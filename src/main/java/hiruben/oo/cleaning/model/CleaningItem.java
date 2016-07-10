package hiruben.oo.cleaning.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 「クリーニング品」です。
 */
public class CleaningItem {
  /** 品目 */
  public final ItemKind kind;
  /** 加工方法 */
  public final ProcessKind[] processes;

  public CleaningItem(ItemKind kind, ProcessKind process, ProcessKind... additonalProcesses) {
    this.kind = kind;
    List<ProcessKind> ps = new ArrayList<ProcessKind>();
    ps.add(process);
    ps.addAll(Arrays.asList(additonalProcesses));
    this.processes = ps.toArray(new ProcessKind[0]);
  }

  @Override
  public int hashCode() {
    return kind.hashCode() + Arrays.hashCode(processes);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CleaningItem)) {
      return false;
    }
    CleaningItem other = (CleaningItem) obj;
    return
        kind == other.kind
        && Arrays.equals(processes, other.processes);
  }

  @Override
  public String toString() {
    return String.format("%s %s", kind, Arrays.toString(processes));
  }
}
