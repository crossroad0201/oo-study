package hiruben.oo.cleaning.model;

/**
 * 返却物です。
 */
public class CollectResult {
  /** 注文 */
  public final Order order;
  /** 今回仕上がっていたクリーニング品 */
  public final CleaningItem[] collectedItems;

  CollectResult(Order order, CleaningItem... items) {
    this.order = order;
    this.collectedItems = items;
  }
}
