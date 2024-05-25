# Команда
- Паничева Валерия
- Яценко Дарья
- Федорова Анастасия
- Воронова Анастасия
  
# Описание
"5 LETTERS" - это клиент-серверная игра, созданная на языке программирования Clojure. 
В этой игре игроки соревнуются в отгадывании загаданного слова из пяти букв. 
Каждый клиент подключается через консоль к центральному серверу, 
где происходит управление игрой и обмен информацией между игроками.

# Правила игры
Вам и другим игрокам нужно отгадать загаданное слово из 5 букв.
Правила игры можно узнать по команде `rules`.

**Начало игры:**

* Сначала нужно зайти в какталог mire и выполнить команду `lein run` (так запустится сервер)
  
  ![image](https://github.com/NonsmeNo/mire/assets/113109207/68ec685f-cbe2-4a6a-bddb-5093f132b8e7)

* Затем игроки смогут подключаться к серверу по `telnet localhost 3333`

  ![image](https://github.com/NonsmeNo/mire/assets/113109207/c4dbe6f9-3943-4cb0-b867-3ca8babbfd05)

* Все, можно играть!
  
**Как играть:**

* Начните игру командой `start`.
* Угадывайте слова командой `word [ваше слово]`.
* Если вы угадали первые буквы загаданного слова, вам выведется сообщение-подсказка - угаданные буквы.
* Команда `skip` нужна, если вы устали и не можете угадать слово.
* Если другой игрок угадает слово раньше вас, вы об этом узнаете. Но у вас есть все шансы стать победителем.


# Пример игры
После того, как игрок подключется, ему нужно оказать свое имя.
Дальше он увидит приветствие:
![image](https://github.com/NonsmeNo/mire/assets/113109207/9c931280-ce74-4674-88d8-6ee2627e290c)


Если игрок не знает / забыл правила игры, он может ввести `rules`:
![image](https://github.com/NonsmeNo/mire/assets/113109207/abc781b3-08f3-479b-a3b3-0189e0057ef6)

Для примера подключим к игре двух игроков с именами: `lera` и `dasha`
Игроки по очереди будут укадывать буквы в слове, называя последовательно слова из 5 букв.
Вот такой результат получился (на рисунке приведена консоль `lera`)
![image](https://github.com/NonsmeNo/mire/assets/113109207/847661a5-5058-4c85-a87f-b77406278907)

Видно, что сервер изначально загадал слово `happy`
Но после долгих отадываний выиграл игром с именем `lera`

Каждому игроку приходят сообщения от сервера с информацией о том, что происходит:
- что делают другие игроки (отгадали или нет и какие слова вводили)
- если кто-то выиграл, игрок сразу узнает об этом

Если игроков будет еще больше, то получится целое соревнование!




# Описание кода
Главная идея такого приложения - использование команд, которые пользователь может отправлять серверу.

В файле `commands.clj` всеэти команды обрабатываются.

Например, следующий код обрабатывает команду `word`
Сервер считвает слово, которое отправил ему пользователь.
Затем производит некоторую проверку и отправляет ответ: были ли угадвны буквы, если да, то какие:

![image](https://github.com/NonsmeNo/mire/assets/113109207/3a760155-c12d-427a-9304-2a49a914b61c)


# __________
# Mire

It's a nonviolent MUD. (Multi-User Dungeon)

## Usage

First make sure that you have `java` installed on your
machine. [OpenJDK](https://adoptopenjdk.net) is recommended. It should
be at least version 8, but newer versions (tested up to 17) should work too.

Do `./lein run` inside the Mire directory to launch the Mire
server. Then players can connect by telnetting to port 3333.

## Motivation

The primary purpose of this codebase is as a demonstration of how to
build a simple multithreaded server in Clojure.

Mire is built up step-by-step, where each step introduces one or two
small yet key Clojure principles and builds on the last step. The
steps each exist in separate git branches. To get the most out of
reading Mire, you should start reading in the branch called
[step-01-echo-server](http://github.com/technomancy/mire/tree/01-echo-server)
and continue from there.

While you can learn from Mire on its own, it has been written
specifically for the [PluralSight screencast on
Clojure](https://www.pluralsight.com/courses/functional-programming-clojure).
A [blog post](https://technomancy.us/136) steps through the codebase
and shows how to make minor updates for a more recent version of Clojure.

Copyright © 2009-2021 Phil Hagelberg
Licensed under the same terms as Clojure.
