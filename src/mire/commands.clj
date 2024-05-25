(ns mire.commands
  (:require [clojure.string :as str]
            [mire.player :as player]))

(defn- move-between-refs
  "Move one instance of obj between from and to. Must call in a transaction."
  [obj from to]
  (alter from disj obj)
  (alter to conj obj))


;; Command functions

(defn look
  "Get a description of the surrounding environs and its contents."
  []
  (str (:desc @player/*current-room*)
       "\n" (keys @(:exits @player/*current-room*)) "\n"
       (str/join "\n" (map #(str "There is " % " here.\n")
                           @(:items @player/*current-room*)))))


(defn discard
  "Put something down that you're carrying."
  [thing]
  (dosync
   (if (player/carrying? thing)
     (do (move-between-refs (keyword thing)
                            player/*inventory*
                            (:items @player/*current-room*))
         (str "You dropped the " thing "."))
     (str "You're not carrying a " thing "."))))


(defn say-server ; Функция, которая показывает сообщение всем пользователям текущей комнаты
  [& words]
  (let [message (str/join " " words)]
    (doseq [inhabitant (disj @(:inhabitants @player/*current-room*)
                             player/*name*)]
      (binding [*out* (player/streams inhabitant)]
        (println message)
        (print player/prompt)))
    (str message)))

; эта команда показывает правила игры
(defn rules []
"Shows rools of game."
  (println "")
  (println "You and the other players need to guess a 5 letter word :)")
  (println "Each player can guess words by writing them into the console.")
  (println "After trying to guess a word, you will receive a message about whether something is guessed or not.")
  (println "")
  (println "--> The 'start' command starts the game")
  (println "--> The 'word [your word]' command is an attempt to guess a word.")
  (println "--> The 'skip' command is needed if you are tired and can't guess the word")
  (println "")
  (println "If another player guesses the word before you, you will know about it. But you have all chances to become the winner!!!")
  (println ""))
 
(defn help
  "Show available commands and what they do."
  []
  (str/join "\n" (map #(str (key %) ": " (:doc (meta (val %))))
                      (dissoc (ns-publics 'mire.commands)
                              'execute 'commands))))

; массив слов из 5 букв
(def words ["apple" "flash" "dream" "happy" "music"
            "glass" "river" "horse" "table" "chair"])

(defonce current-word (atom nil)) ; слово, загаданное сервером

(defn random-word []
  (nth words (rand-int (count words))))

(defn start [] ; начало игры
  (reset! current-word (random-word))
  (say-server "I made up a 5-letter word. You have to guess it!"))

(defn skip [] ; если пользователь не может угадать слово, скипает
  (if @current-word
    (do
      (let [word @current-word]
        (reset! current-word nil)
        (say-server word)))
    (str "No word to skip")))

(defn word [guess] ; функция, которая сравнивает слово от пользователя с current-word
  (say-server player/*name* ": word" guess)
  (if @current-word
    
    (do
      (if (= 5 (count guess)) 
        (do
          (if (= guess @current-word)
            (do
              (reset! current-word nil)
              (say-server "Correct! " player/*name* ", you win!!!"))
            (do
              (let [guessed-letters (loop [i 0, guessed ""]
                                      (if (< i 5)
                                        (if (= (get guess i) (get @current-word i))
                                          (recur (inc i) (say-server guessed (get guess i)))
                                          guessed)
                                        guessed))]
                  (if (empty? guessed-letters)
                    (say-server player/*name*", you didn't guess, try again :(")
                    (say-server player/*name* ", you guessed the first " (/ (count guessed-letters) 2) " letters: " guessed-letters))))))
        (say-server player/*name* ", the word must have 5 letters")))
    (say-server "You need to start the game with the 'start' command first.")))

;; Command data

(def commands {"discard" discard
               "say-server" say-server
               "help" help
               "start" start
               "skip" skip
               "rules" rules
               "word" word})

;; Command handling

(defn execute
  "Execute a command that is passed to us."
  [input]
  (try (let [[command & args] (.split input " +")]
         (apply (commands command) args))
       (catch Exception e
         (.printStackTrace e (new java.io.PrintWriter *err*))
         "Unknown command!")))
