(ns cards.flexbox
  (:require-macros
   [devcards.core :refer [defcard]])
  (:require
   [devcards.core :as dc]
   [sablono.core :as html :refer-macros [html]]))

(def devcardblue "#428bca")

;; small b and d blocks
(def small-i 18)
(def small-px (str small-i "px"))
(defn small? [chr] (#{"b" "d"} chr))

;; big A, C and E blocks
(def big-i (+ small-i 10))
(def big-px (str big-i "px"))

;; abcde-box can fit three blocks per line
(def abcde-i (+ 4 (* 3 (+ 2 big-i))))
(def abcde-px (str abcde-i "px"))

;; column one contains only text
(def col1-i 120)
(def col1-px (str col1-i "px"))

;; column two contains either text or two abcde-boxes
(def col2-i (+ 4 (* 2 (+ 2 abcde-i))))
(def col2-px (str col2-i "px"))

;; column one + column two is the complete interface
(def cols-i (+ 4 col1-i col2-i))
(def cols-px (str cols-i "px"))

(def options
  {:align-content
   ["center"
    "flex-end"
    "flex-start"
    "space-around"
    "space-between"
    "stretch"]
   :align-items
   ["baseline"
    "center"
    "flex-end"
    "flex-start"
    "stretch"]
   :justify-content
   ["center"
    "flex-end"
    "flex-start"
    "space-around"
    "space-between"]})

(defn letter
  [chr]
  (let [size (if (small? chr) small-px big-px)]
    (html
     [:div {:style {:background-color "lightblue"
                    :margin "1px"
                    :min-height size
                    :text-align "center"
                    :min-width size}} chr])))

(defn abcde
  [style bgc]
  (html
   [:div {:style (assoc style
                        :background-color bgc
                        :display "flex"
                        :height abcde-px
                        :margin "1px"
                        :width abcde-px)}
    (map letter "AbCdE")]))

(defn mk-combobox
  [data box-nr k]
  (html
   [:div {:style {:display "flex" :flex-direction "row"}}
    [:div {:style {:width col1-px :color devcardblue}} (name k)]
    [:div {:style {:width col2-px}}
     [:select
      {:on-change (fn [e] (swap! data assoc-in [box-nr k] (.. e -target -value)))
       :value (get-in @data [box-nr k])}
      (map (fn [o] [:option o]) (get options k))]]]))

(defn mk-row
  [{:keys [flex-direction] :as style} bool]
  (html
   [:div {:style {:display "flex" :flex-direction "row"}}
    [:div {:style {:width col1-px}} flex-direction]
    (abcde (assoc style :flex-wrap "wrap") (if bool "orange" "navajowhite"))
    (abcde (assoc style :flex-wrap "wrap-reverse") (if bool "navajowhite" "orange"))]))

(defn mk-flexbox
  [data box-nr]
  (let [style (get @data box-nr)]
    (html
     [:div {:style {:display "flex" :flex-direction "column"}}
      (mk-combobox data box-nr :justify-content)
      (mk-combobox data box-nr :align-content)
      (mk-combobox data box-nr :align-items)
      [:div {:style {:height "30px"}}]
      [:div {:style {:display "flex" :flex-direction "row"}}
       [:div {:style {:width col1-px}}]
       [:div {:style {:width col2-px :color devcardblue}} "flex-wrap"]]
      [:div {:style {:display "flex" :flex-direction "row"}}
       [:div {:style {:width col1-px :color devcardblue}} "flex-direction"]
       [:div {:style {:width abcde-px}} "wrap"]
       [:div {:style {:width abcde-px}} "wrap-reverse"]]
      (mk-row (assoc style :flex-direction "row") true)
      (mk-row (assoc style :flex-direction "column") false)
      (mk-row (assoc style :flex-direction "row-reverse") false)
      (mk-row (assoc style :flex-direction "column-reverse") true)])))

(defonce state
  (atom
   {1 {:align-content "flex-start"
       :align-items "flex-start"
       :justify-content "flex-start"}
    2 {:align-content "flex-start"
       :align-items "flex-start"
       :justify-content "flex-start"}}))

(defcard flexboxes
  (fn [data _]
    (html
     [:div {:style {:display "flex"
                    :flex-direction "row"
                    :justify-content "space-between"}}
      (mk-flexbox data 1)
      (mk-flexbox data 2)]))
  state)

(defcard summary
  "
### Main-axis items

* Align items along main-axis with `justify-content`
* Flip items over main-axis by adding `-reverse` to `flex-direction`

### Cross-axis lines

* Align lines along cross-axis with `align-content`
* Flip cross-axis lines by adding `-reverse` to `flex-wrap`

### Cross-axis items

* Align items along cross-axis with `align-items`

### 90 degree turns

Note how the block colors highlight 90 degree turns, eg;

* row wrap
* column wrap-reverse
* row-reverse wrap-reverse
* column-reverse wrap")
