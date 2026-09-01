(ns core-async-quest.lesson-test
  (:require
    [cljs.test :refer-macros [deftest is testing]]
    [core-async-quest.lesson :as lesson]))


(deftest chapter-describes-a-rendezvous
  (testing "the introductory content is complete"
    (is (= "01" (:number lesson/chapter)))
    (is (= 4 (count lesson/events)))
    (is (= :taker (get-in lesson/chapter [:check :correct])))
    (is (= 3 (count (get-in lesson/chapter [:check :answers]))))
    (is (= 3 (count (:foundations lesson/chapter))))
    (is (= 4 (count (:applications lesson/chapter))))
    (is (every? #(contains? % :situation) (:applications lesson/chapter)))
    (is (every? #(contains? % :flow) (:applications lesson/chapter)))
    (is (every? #(contains? % :benefit) (:applications lesson/chapter)))
    (is (string? (:overview lesson/chapter)))
    (is (string? (:next lesson/chapter)))))


(deftest advancing-moves-one-event-at-a-time
  (let [first-step (lesson/advance lesson/initial-db)
        second-step (lesson/advance first-step)]
    (is (= 0 (:step lesson/initial-db)))
    (is (= 1 (:step first-step)))
    (is (= 2 (:step second-step)))))


(deftest advancing-stops-at-the-final-event
  (let [completed (assoc lesson/initial-db :step (lesson/last-step))]
    (is (lesson/complete? completed))
    (is (= completed (lesson/advance completed)))))


(deftest reset-restores-a-fresh-chapter
  (let [played (assoc lesson/initial-db :step 3 :answer :buffer :attempts 2)]
    (is (= lesson/initial-db (lesson/reset played)))))


(deftest answers-are-recorded-and-counted
  (let [after-first (lesson/answer lesson/initial-db :buffer)
        after-second (lesson/answer after-first :taker)]
    (is (= :buffer (:answer after-first)))
    (is (= 1 (:attempts after-first)))
    (is (= :taker (:answer after-second)))
    (is (= 2 (:attempts after-second)))))


(deftest correctness-depends-on-selected-answer (is (not (lesson/correct-answer? (lesson/answer lesson/initial-db :clock)))) (is (lesson/correct-answer? (lesson/answer lesson/initial-db :taker))))


(deftest chapter-two-introduces-go-parking
  (let [chapter-db (lesson/select-chapter lesson/initial-db "02")]
    (is (= "02" (get-in chapter-db [:chapter :number])))
    (is (= 5 (count (lesson/events-for (:chapter chapter-db)))))
    (is (= :park (get-in chapter-db [:chapter :check :correct])))
    (is (string? (get-in chapter-db [:chapter :code])))
    (is (re-find #"still blocks" (get-in chapter-db [:chapter :caution])))))


(deftest chapter-two-has-its-own-final-step
  (let [chapter-db (lesson/select-chapter lesson/initial-db "02")
        complete-db (assoc chapter-db :step (lesson/last-step chapter-db))]
    (is (= 4 (lesson/last-step chapter-db)))
    (is (lesson/complete? complete-db))
    (is (= complete-db (lesson/advance complete-db)))))
