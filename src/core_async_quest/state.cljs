(ns core-async-quest.state
  (:require
    [core-async-quest.lesson :as lesson]
    [re-frame.core :as rf]))


(defn initialize-db
  []
  lesson/initial-db)


(defn advance-db
  [db]
  (lesson/advance db))


(defn reset-db
  [db]
  (lesson/reset db))


(defn answer-db
  [db answer-id]
  (lesson/answer db answer-id))


(rf/reg-event-db :initialize (fn [_ _] (initialize-db)))
(rf/reg-event-db :advance (fn [db _] (advance-db db)))
(rf/reg-event-db :reset-lesson (fn [db _] (reset-db db)))
(rf/reg-event-db :answer (fn [db [_ answer-id]] (answer-db db answer-id)))
(rf/reg-sub :chapter (fn [db _] (:chapter db)))
(rf/reg-sub :step (fn [db _] (:step db)))
(rf/reg-sub :answer (fn [db _] (:answer db)))
(rf/reg-sub :attempts (fn [db _] (:attempts db)))
