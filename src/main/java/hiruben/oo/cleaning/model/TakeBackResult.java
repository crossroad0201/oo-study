package hiruben.oo.cleaning.model;

/**
 * 返却物です。
 */
public class TakeBackResult {
  /** 注文 */
  public final Order order;
  /** 今回仕上がっていたクリーニング品 */
  public final CleaningItem[] processedItems;

  TakeBackResult(Order order, CleaningItem... items) {
    this.order = order;
    this.processedItems = items;
  }
}
