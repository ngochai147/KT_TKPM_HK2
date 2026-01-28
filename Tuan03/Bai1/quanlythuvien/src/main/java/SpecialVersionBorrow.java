public class SpecialVersionBorrow extends BorrowDecorator {
    public SpecialVersionBorrow(Borrow borrow) {
        super(borrow);
    }

    public String borrowInfo() {
        return borrow.borrowInfo() + " + Phiên bản đặc biệt";
    }
}
