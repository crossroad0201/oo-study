package hiruben.oo.cleaning.model;

import java.util.Optional;

/**
 * 返却物です。
 */
public class CollectResult {
  /** チケット */
  public final Optional<Ticket> ticket;
  /** 今回仕上がっていたクリーニング品 */
  public final CleaningItem[] items;

  private CollectResult(Optional<Ticket> ticket, CleaningItem... items) {
    this.ticket = ticket;
    this.items = items;
  }

  static CollectResult remainingItems(Ticket ticket, CleaningItem... items) {
    return new CollectResult(Optional.of(ticket), items);
  }

  static CollectResult noRemainingItems(CleaningItem... items) {
    return new CollectResult(Optional.empty(), items);
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder()
        .append(String.format("%s\n", ticket.isPresent() ? ticket.get() : "チケットなし"));
    s.append("返却 : \n");
    for (CleaningItem item : items) {
      s.append(String.format("  %s\n", item));
    }
    return s.toString();
  }
}
