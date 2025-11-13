// dom.js
export const $ = (sel, el = document) => el.querySelector(sel);
export const $$ = (sel, el = document) => [...el.querySelectorAll(sel)];

export function on(target, event, handler) {
  target.addEventListener(event, handler);
}
