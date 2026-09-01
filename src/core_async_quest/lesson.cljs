(ns core-async-quest.lesson)

(def events [{:at 0 :kind :process :label "A producer has a message: 42."} {:at 1 :kind :channel :label "The channel has no receiver waiting yet."} {:at 2 :kind :process :label "A consumer asks the channel for its next message."} {:at 3 :kind :success :label "The message is handed over; both processes continue."}])


(def chapter
  {:number "01"
   :title "Messages, not waiting"
   :concept "core.async is a Clojure(script) library for coordinating independent pieces of work by passing messages through channels."
   :overview "It gives your program a clear way to say who produces a value, who needs it next, and how those two sides coordinate without sharing mutable state."
   :foundations [{:title "Processes" :body "Independent pieces of work. A process might fetch data, respond to a click, or save a document."}
                 {:title "Channels" :body "Named handoff points for values. One process puts a value in; another takes it out."}
                 {:title "Messages" :body "The values travelling through channels: a result, an event, a request, or a signal that work is done."}]
   :applications [{:title "A responsive search box"
                   :situation "A person types quickly while the app is still waiting for an earlier API response."
                   :flow "Keystrokes and search results arrive as separate messages; the UI process decides which result is still relevant."
                   :benefit "The event flow is explicit, so stale responses do not need to be hidden in nested callbacks."}
                  {:title "Sending confirmation emails"
                   :situation "A web request creates an account, but sending email should not make the person wait."
                   :flow "The request process hands an email job to a channel; a background worker receives and sends it."
                   :benefit "Request handling and slow background work stay separate while sharing a clear handoff."}
                  {:title "Processing imported data"
                   :situation "A CSV file must be read, validated, transformed, and stored without loading every row into memory at once."
                   :flow "Each stage receives one row from a channel, does its work, then passes the result to the next stage."
                   :benefit "The pipeline mirrors the business steps and naturally controls how much work is in progress."}
                  {:title "Coordinating uploads and cancellation"
                   :situation "Several files upload in the background and a person may cancel one or all of them."
                   :flow "Upload progress, completion, errors, and cancellation requests are messages handled by the relevant processes."
                   :benefit "The UI can react to each state change without sharing flags across unrelated tasks."}]
   :goal "Advance the trace to watch one message move from a producer to a consumer."
   :next "Later chapters introduce channel buffers, `go` blocks, and the `>!` / `<!` operations used to put and take values."
   :check {:question "When may the producer continue past an unbuffered channel handoff?" :answers [{:id :buffer :label "As soon as a value is placed in the channel"} {:id :taker :label "When a consumer takes the value"} {:id :clock :label "After one scheduler tick"}] :correct :taker :explanation "Exactly. With no buffer, the put and take complete together; this meeting is the rendezvous."}})


(def initial-db {:chapter chapter :step 0 :answer nil :attempts 0})


(defn last-step
  []
  (dec (count events)))


(defn complete?
  [db]
  (= (:step db) (last-step)))


(defn advance
  [db]
  (update db :step #(min (inc %) (last-step))))


(defn reset
  [db]
  (assoc db :step 0 :answer nil :attempts 0))


(defn answer
  [db answer-id]
  (update (assoc db :answer answer-id) :attempts inc))


(defn correct-answer?
  [db]
  (= (:answer db) (get-in db [:chapter :check :correct])))
