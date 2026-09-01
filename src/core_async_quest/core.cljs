(ns core-async-quest.core
  (:require
    [re-frame.core :as rf]
    [reagent.dom :as rdom]))


(def initial-db
  {:lesson {:title "The rendezvous"
            :concept "An unbuffered channel lets a put and take meet."
            :goal "Advance the clock until the message is delivered."}
   :step 0
   :events [{:at 0 :kind :process :label "Producer is ready to put 42"}
            {:at 1 :kind :channel :label "Channel waits: no taker yet"}
            {:at 2 :kind :process :label "Consumer takes 42"}
            {:at 3 :kind :success :label "Rendezvous complete"}]})


(rf/reg-event-db :initialize (fn [_ _] initial-db))
(rf/reg-event-db :advance (fn [db _] (update db :step #(min (inc %) (dec (count (:events db)))))))
(rf/reg-event-db :reset-lesson (fn [db _] (assoc db :step 0)))
(rf/reg-sub :lesson (fn [db _] (:lesson db)))
(rf/reg-sub :step (fn [db _] (:step db)))
(rf/reg-sub :events (fn [db _] (:events db)))


(defn event-card
  [{:keys [at kind label]} active-step]
  [:li.event-card {:class (when (<= at active-step) "is-active")}
   [:span.event-time (str "t" at)]
   [:span.event-icon (case kind :channel "↔" :success "★" "●")]
   [:span label]])


(defn app
  []
  (let [lesson @(rf/subscribe [:lesson]) step @(rf/subscribe [:step]) events @(rf/subscribe [:events]) complete? (= step (dec (count events)))]
    [:main.app-shell
     [:section.hero [:p.eyebrow "CORE.ASYNC // LEVEL 01"] [:h1 (:title lesson)] [:p (:concept lesson)]]
     [:section.lesson-panel
      [:div.quest-copy
       [:h2 "Mission"] [:p (:goal lesson)]
       [:div.channel-scene [:div.process.producer "Producer"] [:div.channel (if (>= step 2) "42" "…")] [:div.process.consumer "Consumer"]]
       [:button.primary-button {:on-click #(rf/dispatch [:advance]) :disabled complete?} (if complete? "Quest complete!" "Advance simulation →")]
       [:button.text-button {:on-click #(rf/dispatch [:reset-lesson])} "Reset"]]
      [:aside.timeline [:h2 "Trace"] [:ol (for [event events] ^{:key (:at event)} [event-card event step])]]]
     [:footer "Next: buffered channels, parking, and back-pressure."]]))


(defn mount!
  []
  (rf/dispatch-sync [:initialize]) (rdom/render [app] (.getElementById js/document "app")))


(defn ^:export init
  []
  (mount!))
