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


(def chapter-two
  {:number "02"
   :title "Go, then park"
   :concept "A `go` block describes a lightweight process. When `<!` or `>!` cannot proceed, that process parks and resumes later instead of holding up a thread."
   :overview "Chapter 1 showed a channel handoff. Now we give the waiting work a home: a `go` block. This makes a sequence of asynchronous steps read from top to bottom while the runtime handles the pause and resume."
   :foundations [{:title "`go` blocks" :body "A `go` block starts a lightweight process. Use it for coordination around channels, not for long CPU work or ordinary blocking I/O."}
                 {:title "`<!` takes" :body "Inside `go`, `<!` asks for the next value from a channel. If no value is ready, the go process parks at that line."}
                 {:title "`>!` puts" :body "Inside `go`, `>!` hands a value to a channel. It parks until that handoff can complete when the channel has no capacity or taker."}]
   :applications [{:title "Load, shape, then render" :situation "A screen needs a profile from one asynchronous source before it can prepare a view model." :flow "A go process takes the profile with `<!`, selects the needed fields, then puts the view model with `>!`." :benefit "The dependent steps stay in one readable sequence instead of being split across callback boundaries."}
                  {:title "Wait for a save result" :situation "A form submits work and must react when the result eventually arrives." :flow "The coordination process parks on a result channel, then emits either a success or error message." :benefit "Waiting is explicit and does not require a shared `loading?` flag to control every branch."}
                  {:title "Stage a small workflow" :situation "A task needs one input before it can begin the next channel-based step." :flow "Each `<!` / `>!` marks a real handoff; the process resumes exactly where it parked." :benefit "The code exposes the order of dependency without blocking a thread for each paused task."}]
   :events [{:at 0 :kind :process :label "The `go` process starts and asks for a profile with `<!`."} {:at 1 :kind :channel :label "No profile is ready, so the go process parks at the take."} {:at 2 :kind :process :label "Other work can continue while this process is parked."} {:at 3 :kind :channel :label "A profile arrives; the process resumes with that value."} {:at 4 :kind :success :label "It prepares a view model and hands it off with `>!`."}]
   :goal "Advance the trace and notice the difference between a parked go process and a blocked thread."
   :code "(go\n  (let [profile (<! profile-ch)\n        view    (select-keys profile [:name :role])]\n    (>! view-ch view)))"
   :caution "Parking only happens at core.async channel operations inside `go`. A slow calculation or blocking call you write inside a go block still blocks."
   :next "Next, buffers change the timing of a handoff and let us talk precisely about throughput and back-pressure."
   :check {:question "What happens when `<!` has no value available inside a `go` block?" :answers [{:id :park :label "The go process parks and resumes when a value is available"} {:id :thread :label "It blocks the underlying thread until a value arrives"} {:id :skip :label "It skips the take and returns nil"}] :correct :park :explanation "Right. The go process pauses at the channel operation; the runtime can use the underlying execution resource for other work."}})


(def chapters [chapter chapter-two])


(defn chapter-by-number
  [number]
  (or (some #(when (= number (:number %)) %) chapters) chapter))


(defn events-for
  [chapter]
  (or (:events chapter) events))


(def initial-db {:chapter chapter :step 0 :answer nil :attempts 0})


(defn last-step
  ([] (dec (count events)))
  ([db] (dec (count (events-for (:chapter db))))))


(defn complete?
  [db]
  (= (:step db) (last-step db)))


(defn advance
  [db]
  (update db :step #(min (inc %) (last-step db))))


(defn reset
  [db]
  (assoc db :step 0 :answer nil :attempts 0))


(defn answer
  [db answer-id]
  (update (assoc db :answer answer-id) :attempts inc))


(defn select-chapter
  [_ number]
  {:chapter (chapter-by-number number) :step 0 :answer nil :attempts 0})


(defn correct-answer?
  [db]
  (= (:answer db) (get-in db [:chapter :check :correct])))
