# quickHilight
highlight quickly for chinese

- AcAutoHighLight
  - 基于HanLP的双数组Trie树的AhoCorasick自动机实现的关键字高亮， 性能较差，即便是短句+几个关键词，也需要每次几十MB。
  - Mac 2020，16G，intel i5 高亮示例中30字6关键词，性能仅在 1400次每秒。
  - 但该方法使用AhoCorasick，故在长句应表现优秀， 尤其是匹配关键词有数万甚至百万时。
  - 功能多，修改下，可以支持模糊词、忽略词等
- QuickHighlight
  - 基于String index， 最长区间高亮
  - 支持忽略英文大小写、支持繁体字
  - 短句时性能好，Mac 2020，16G，intel i5 高亮示例中30字6关键词，性能可达到 50_0000次每秒。

上述两个类都支持中文、最长匹配(最长短语高亮)