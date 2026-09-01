(ns core-async-quest.core
  (:require
    [core-async-quest.lesson :as lesson]
    [core-async-quest.state]
    [re-frame.core :as rf]
    [reagent.dom :as rdom]))


(def document-style {:margin "0" :min-width "320px" :background "#111827" :color "#eef2ff" :font-family "Inter, ui-sans-serif, system-ui, sans-serif"})


(def styles
  {:app {:width "min(100% - 2rem, 1050px)" :margin "0 auto" :padding "4rem 0 2rem"}
   :eyebrow {:color "#67e8f9" :font-size ".75rem" :font-weight 800 :letter-spacing ".14em"}
   :hero {:max-width "44rem"}
   :title {:margin ".25rem 0" :font-size "clamp(2.4rem, 7vw, 4.5rem)" :letter-spacing "-.06em"}
   :copy {:color "#cbd5e1" :font-size "1.1rem" :line-height 1.6}
   :panel {:display "flex" :flex-wrap "wrap" :gap "1rem" :margin-top "2rem"}
   :card {:flex "1 1 22rem" :border "1px solid #334155" :border-radius "1.25rem" :background "#172033" :padding "1.5rem"}
   :scene {:display "flex" :align-items "center" :justify-content "space-between" :gap ".5rem" :margin "2rem 0" :text-align "center"}
   :process {:border-radius "999px" :padding "1rem .8rem" :font-weight 750 :background "#312e81" :border "1px solid #818cf8"}
   :channel {:min-width "4rem" :border-radius "999px" :padding "1rem .8rem" :font-weight 750 :background "#083344" :border "1px dashed #67e8f9" :color "#67e8f9" :font-size "1.4rem"}
   :primary {:border 0 :border-radius ".7rem" :padding ".8rem 1rem" :background "#67e8f9" :color "#082f49" :font-weight 800 :cursor "pointer"}
   :text-button {:margin-left ".75rem" :border 0 :color "#cbd5e1" :background "transparent" :text-decoration "underline" :cursor "pointer"}
   :timeline {:margin 0 :padding 0 :list-style "none"}
   :event {:display "grid" :grid-template-columns "2rem 1.5rem 1fr" :gap ".4rem" :align-items "center" :padding ".75rem 0" :color "#64748b" :border-bottom "1px solid #273449"}
   :code {:overflow-x "auto" :margin "1.25rem 0" :padding "1rem" :border "1px solid #334155" :border-radius ".75rem" :background "#0b1220" :color "#a5f3fc" :line-height 1.6}
   :answers {:display "grid" :gap ".65rem"}
   :answer {:padding ".8rem 1rem" :border "1px solid #475569" :border-radius ".7rem" :background "#1e293b" :color "#e2e8f0" :text-align "left" :cursor "pointer"}
   :footer {:margin-top "1.5rem" :color "#94a3b8" :font-size ".9rem"}
   :foundation-grid {:display "flex" :flex-wrap "wrap" :gap "1rem" :margin-top "2rem"}
   :foundation-card {:flex "1 1 15rem" :padding "1rem" :border "1px solid #334155" :border-radius ".9rem" :background "#172033"}
   :applications {:display "grid" :gap "1rem" :margin-top "1rem"}
   :application-card {:padding "1rem" :border-left "3px solid #67e8f9" :background "#0f172a" :border-radius ".25rem"}})


(defn merge-style
  [& style-maps]
  (apply merge style-maps))


(defn event-card
  [{:keys [at kind label]} active-step]
  [:li {:style (merge-style (:event styles) (when (<= at active-step) {:color "#f8fafc"}))}
   [:span {:style {:font-family "ui-monospace, monospace" :font-size ".75rem" :font-weight 700 :color "#67e8f9"}} (str "t" at)]
   [:span {:style {:text-align "center"}} (case kind :channel "↔" :success "★" "●")]
   [:span label]])


(defn knowledge-check
  [chapter answer attempts]
  (let [{:keys [question answers explanation]}
        (:check chapter)
        correct? (= answer (get-in chapter [:check :correct]))]
    [:section {:style (merge-style (:card styles) {:margin-top "1rem" :border-color "#0e7490"})}
     [:p {:style (:eyebrow styles)} "CHECKPOINT"] [:h2 question]
     [:div {:style (:answers styles)} (for [{:keys [id label]} answers]
                                        ^{:key id} [:button {:style (merge-style (:answer styles) (when (= id answer) (if correct? {:border-color "#34d399" :background "#064e3b"} {:border-color "#fbbf24"}))) :aria-pressed (= id answer) :on-click #(rf/dispatch [:answer id])} label])]
     (when answer [:p {:style {:margin-bottom 0 :color (if correct? "#6ee7b7" "#fcd34d")}} (if correct? explanation (str "Not quite — try again. Attempts: " attempts "."))])]))


(defn chapter-introduction
  [chapter]
  [:section
   [:p {:style (:overview styles)} (:overview chapter)]
   [:div {:style (:foundation-grid styles)}
    (for [{:keys [title body]} (:foundations chapter)]
      ^{:key title} [:article {:style (:foundation-card styles)} [:h2 title] [:p {:style (:copy styles)} body]])]
   [:section {:style (merge-style (:card styles) {:margin-top "1rem"})}
    [:p {:style (:eyebrow styles)} "WHERE IT HELPS"]
    [:h2 "A practical coordination tool"]
    [:div {:style (:applications styles)}
     (for [{:keys [title situation flow benefit]} (:applications chapter)]
       ^{:key title}
       [:article {:style (:application-card styles)}
        [:h3 {:style {:margin "0 0 .5rem"}} title]
        [:p {:style (:copy styles)} [:strong "Situation: "] situation]
        [:p {:style (:copy styles)} [:strong "Message flow: "] flow]
        [:p {:style (merge-style (:copy styles) {:margin-bottom 0})} [:strong "Why it helps: "] benefit]])]
    [:p {:style (:copy styles)} (:next chapter)]]])


(defn app
  []
  (let [chapter @(rf/subscribe [:chapter])
        step @(rf/subscribe [:step])
        answer @(rf/subscribe [:answer])
        attempts @(rf/subscribe [:attempts])
        complete? (lesson/complete? {:step step})]
    [:main {:style (:app styles)}
     [:section {:style (:hero styles)} [:p {:style (:eyebrow styles)} (str "CORE.ASYNC // CHAPTER " (:number chapter))] [:h1 {:style (:title styles)} (:title chapter)] [:p {:style (:copy styles)} (:concept chapter)]]
     [chapter-introduction chapter]
     [:section {:style (:panel styles)}
      [:div {:style (:card styles)} [:p {:style (:eyebrow styles)} "MISSION"] [:h2 "Make two processes meet"] [:p {:style (:copy styles)} (:goal chapter)]
       [:div {:style (:scene styles)} [:div {:style (:process styles)} "Producer"] [:div {:style (:channel styles)} (if (>= step 2) "42" "…")] [:div {:style (:process styles)} "Consumer"]]
       [:button {:style (merge-style (:primary styles) (when complete? {:background "#a7f3d0" :cursor "default"})) :on-click #(rf/dispatch [:advance]) :disabled complete?} (if complete? "Trace complete" "Advance simulation →")]
       [:button {:style (:text-button styles) :on-click #(rf/dispatch [:reset-lesson])} "Reset chapter"]]
      [:aside {:style (:card styles)} [:p {:style (:eyebrow styles)} "TRACE"] [:h2 "What happens next?"] [:ol {:style (:timeline styles)} (for [event lesson/events] ^{:key (:at event)} [event-card event step])]]]
     (when complete? [knowledge-check chapter answer attempts])
     [:footer {:style (:footer styles)} "Chapter 1 of Core Async Quest · Next: buffered channels and back-pressure."]]))


(defn mount!
  []
  (doseq [[property value] document-style]
    (.setProperty (.-style js/document.body) (name property) value))
  (rf/dispatch-sync [:initialize]) (rdom/render [app] (.getElementById js/document "app")))


(defn ^:export init
  []
  (mount!))
