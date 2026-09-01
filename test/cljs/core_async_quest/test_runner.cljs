(ns core-async-quest.test-runner
  (:require
    [cljs.test :as test]
    [core-async-quest.integration-test]
    [core-async-quest.lesson-test]))


(defn ^:export init
  []
  (let [result (test/run-tests 'core-async-quest.lesson-test
                               'core-async-quest.integration-test)
        passed? (test/successful? result)
        status (.getElementById js/document "test-result")]
    (set! (.-textContent status) (if passed? "passed" "failed"))
    (.setAttribute (.-body js/document) "data-test-result" (if passed? "passed" "failed"))))
