package hiruben.oo.cleaning.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 「価格表」です。
 */
public class PriceList {
  private final List<Menu> menus = new ArrayList<>();

  /**
   * 品目ごとのメニューを追加します。
   *
   * @param process 加工
   * @param itemKind 品目
   * @param price 価格
   * @return 自身
   */
  public PriceList addMenu(Process process, ItemKind itemKind, int price) {
    menus.add(Menu.forItem(process, itemKind, price));
    return this;
  }

  /**
   * すべての品目で共通のメニューを追加します。
   *
   * @param process 加工
   * @param price 価格
   * @return 自身
   */
  public PriceList addMenu(Process process, int price) {
    menus.add(Menu.forAllItem(process, price));
    return this;
  }

  /**
   * メニューを返します。
   *
   * @param process 加工
   * @param kind 品目
   * @return メニュー
   */
  public Optional<Menu> findMenu(Process process, ItemKind kind) {
    // 加工 + 品目 で探す
    for (Menu menu : menus) {
      if (menu.kind.isPresent() && menu.kind.get() == kind && menu.process == process) {
        return Optional.of(menu);
      }
    }

    // なければ、加工 で探す
    for (Menu menu : menus) {
      if (!menu.kind.isPresent() && menu.process == process) {
        return Optional.of(menu);
      }
    }

    return Optional.empty();
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder()
        .append("価格表 : \n");

    for (Menu m : menus) {
      s.append(String.format("  %s\n", m));
    }

    return s.toString();
  }

  /**
   * 「メニュー」です。
   */
  public static class Menu {
    /** 加工 */
    public final Process process;
    /** 品目 */
    public final Optional<ItemKind> kind;
    /** 価格 */
    public final int price;

    private Menu(Process process, Optional<ItemKind> kind, int price) {
      this.process = process;
      this.kind = kind;
      this.price = price;
    }

    static Menu forItem(Process process, ItemKind itemKind, int price) {
      return new Menu(process, Optional.of(itemKind), price);
    }

    static Menu forAllItem(Process process, int price) {
      return new Menu(process, Optional.empty(), price);
    }

    @Override
    public String toString() {
      return String.format("%s %s %s", process, kind.isPresent() ? kind.get() : "All", price);
    }
  }

}

