(ns test-rooms
  (:use [mire.rooms] :reload-all)
  (:use [clojure.test]))

(defn room-fixture [f]
  (binding [rooms (load-rooms {} "resources/rooms/")]
    (f)))

(use-fixtures :each room-fixture)

(deftest test-set-rooms
  (doseq [name [:start :closet :hallway :promenade]]
    (is (contains? rooms name)))
  (is (re-find #"promenade" (:desc (:promenade rooms))))
  (is (= :hallway (:west @(:exits (:promenade rooms)))))
  (is (some #{:bunny} @(:items (:promenade rooms))))
  (is (empty? @(:inhabitants (:promenade rooms)))))

(deftest test-room-contains?
  (let [closet (:closet rooms)]
    (is (not (empty? (filter #(= % :keys) @(:items closet)))))
    (is (room-contains? closet "keys"))
    (is (not (room-contains? closet "monkey")))))