(ns core-async-quest.integration-test
  (:require
    [cljs.test :refer-macros [deftest is testing]]
    [core-async-quest.lesson :as lesson]
    [core-async-quest.state]
    [re-frame.core :as rf]
    [re-frame.db :as rfdb]))


(defn fresh-app!
  []
  (reset! rfdb/app-db {})
  (rf/dispatch-sync [:initialize]))


(deftest initialization-is-wired-through-re-frame
  (fresh-app!)
  (is (= lesson/initial-db @rfdb/app-db))
  (is (= 0 (:step @rfdb/app-db)))
  (is (= lesson/chapter (:chapter @rfdb/app-db))))


(deftest advancing-the-trace-uses-the-registered-event
  (fresh-app!)
  (rf/dispatch-sync [:advance])
  (rf/dispatch-sync [:advance])
  (is (= 2 (:step @rfdb/app-db)))
  (rf/dispatch-sync [:advance])
  (rf/dispatch-sync [:advance])
  (testing "the final event is retained when the learner advances again"
    (is (= (lesson/last-step) (:step @rfdb/app-db)))
    (is (lesson/complete? @rfdb/app-db))))


(deftest checkpoint-answers-flow-through-events-and-subscriptions
  (fresh-app!)
  (rf/dispatch-sync [:answer :buffer])
  (is (= :buffer (:answer @rfdb/app-db)))
  (is (= 1 (:attempts @rfdb/app-db)))
  (is (not (lesson/correct-answer? @rfdb/app-db)))
  (rf/dispatch-sync [:answer :taker])
  (is (= :taker (:answer @rfdb/app-db)))
  (is (= 2 (:attempts @rfdb/app-db)))
  (is (lesson/correct-answer? @rfdb/app-db)))


(deftest reset-event-clears-a-completed-chapter
  (fresh-app!)
  (dotimes [_ 3] (rf/dispatch-sync [:advance]))
  (rf/dispatch-sync [:answer :taker])
  (rf/dispatch-sync [:reset-lesson])
  (is (= lesson/initial-db @rfdb/app-db)))
