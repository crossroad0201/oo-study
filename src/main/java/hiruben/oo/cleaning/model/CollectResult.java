package hiruben.oo.cleaning.model;

/**
 * 返却物です。
 */
public class CollectResult {
  /** チケット */
  public final Ticket ticket; // TODO Optionalにする？
  /** 今回仕上がっていたクリーニング品 */
  public final CleaningItem[] items;

  private CollectResult(Ticket ticket, CleaningItem... items) {
    this.ticket = ticket;
    this.items = items;
  }

  static CollectResult remainingItems(Ticket ticket, CleaningItem... items) {
    return new CollectResult(ticket, items);
  }

  static CollectResult noRemainingItems(CleaningItem... items) {
    return new CollectResult(null, items);
  }
}
