public class ExtendedTimeBorrow extends BorrowDecorator {
    public ExtendedTimeBorrow(Borrow borrow) {
        super(borrow);
    }

    public String borrowInfo() {
        return borrow.borrowInfo() + " + Gia hạn thời gian";
    }
}

