package hiruben.oo.cleaning.model;

import java.util.Arrays;

/**
 * 「クリーニング品」です。
 */
public class CleaningItem {
  /** 名前 */
  public final String name;
  /** 品目 */
  public final Item item;
  /** 加工方法 */
  public final Process[] processes;

  public CleaningItem(String name, Item item, Process... processes) {
    this.name = name;
    this.item = item;
    this.processes = processes;
  }

  @Override
  public int hashCode() {
    return name.hashCode() + item.hashCode() + Arrays.hashCode(processes);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CleaningItem)) {
      return false;
    }
    CleaningItem other = (CleaningItem) obj;
    return
        this.name.equals(other.name)
        && this.item == other.item
        && Arrays.equals(this.processes, other.processes);
  }
}
