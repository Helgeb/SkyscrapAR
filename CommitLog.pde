class CommitLog {
  XMLElement elem;
  
  public CommitLog(XMLElement elem) {
    this.elem = elem;
  }
  
  XMLElement getVersion(int version) {
    XMLElement[] children = this.elem.getChildren();
    if (version > children.length)
      version = children.length;
    else if (version < 1)
      version = 1;

    return children[version - 1];
  }
      
  String getDate() {
    return getVersion(g_currentVersion).getString("date");
  }
}
