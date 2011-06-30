// Code from Visualizing Data, First Edition, Copyright 2008 Ben Fry.


class WordItem extends SimpleMapItem {
  String word;
  color currentColor;
  
  color DEFAULT_COLOR = 0xccCCCCCC; // first two digits is alpha
  color HIGHLIGHT_COLOR = 0xffFFFF99;

  WordItem(String word) {
    this.word = word;
    this.currentColor = DEFAULT_COLOR;
  }

  boolean isSelected() {
    return this.currentColor == HIGHLIGHT_COLOR;
  }

  void toggleSelect() {
    if (this.currentColor == DEFAULT_COLOR)
      this.currentColor = HIGHLIGHT_COLOR;
    else
      this.currentColor = DEFAULT_COLOR;
  }

  void draw() {
    fill(255);
    rect(x, y, w, h);

    fill(0);
    if (w > textWidth(word) + 6) {
      if (h > textAscent() + 6) {
        textAlign(CENTER, CENTER);
        text(word, x + w/2, y + h/2);
      }
    }
  }
}
