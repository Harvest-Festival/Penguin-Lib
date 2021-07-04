package uk.joshiejack.penguinlib.client.gui.book.page;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.widget.ArrowButton;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractMultiPage<P> extends AbstractPage {
    protected List<P> entries = new ArrayList<>();
    protected final int perPage;
    protected int page;

    public AbstractMultiPage(ITextComponent name, int perPage) {
        super(name);
        this.perPage = perPage;
    }

    public void back(Book book) {
        page--;
        book.init(book.minecraft(), book.width, book.height);
    }

    public void forward(Book book) {
        page++;

        book.init(book.minecraft(), book.width, book.height);
    }

    protected abstract void initEntry(Book book, int left, int top, int id, P p);

    protected abstract List<P> getEntries();

    @OnlyIn(Dist.CLIENT)
    public abstract static class Left<P> extends AbstractMultiPage<P> {
        public Left(ITextComponent name, int perPage) {
            super(name, perPage);
        }

        @Override
        public void initLeft(Book book, int left, int top) {
            //IF PAGE != 0, Display Left Button
            if (page != 0)
                book.addButton(new ArrowButton.Left(book, left + 20, top + 154, (button) -> back(book)));
            //IF PAGE != MAX_PAGE, Display Right Button
            entries = getEntries();
            int maxPage = Math.max(0, (int) Math.ceil(((double) entries.size())/perPage) - 1);
            if (page != maxPage)
                book.addButton(new ArrowButton.Right(book, left + 130, top + 154, (button) -> forward(book)));
            //IF PAGE != 0, Display Left Button
            for (int i = 0; i < perPage; i++) {
                int id = (page * perPage) + i;
                if (id < entries.size())
                    initEntry(book, left, top, i, entries.get(id));
            }
        }

        @Override
        public void initRight(Book book, int left, int top) {}
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Right<P> extends AbstractMultiPage<P> {
        public Right(ITextComponent name, int perPage) {
            super(name, perPage);
        }

        @Override
        public void initLeft(Book book, int left, int top) {}

        @Override
        public void initRight(Book book, int left, int top) {
            //IF PAGE != 0, Display Left Button
            if (page != 0)
                book.addButton(new ArrowButton.Left(book, left + 10, top + 154, (button) -> back(book)));
            //IF PAGE != MAX_PAGE, Display Right Button
            entries = getEntries();
            int maxPage = Math.max(0, (int) Math.ceil(((double) entries.size())/perPage) - 1);
            if (page != maxPage)
                book.addButton(new ArrowButton.Right(book, left + 119, top + 154, (button) -> forward(book)));
            //IF PAGE != 0, Display Left Button
            for (int i = 0; i < perPage; i++) {
                int id = (page * perPage) + i;
                if (id < entries.size())
                    initEntry(book, left, top, i, entries.get(id));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Both<P> extends AbstractMultiPage<P> {
        public Both(ITextComponent name, int perPage) {
            super(name, perPage);
        }

        @Override
        public void initLeft(Book book, int left, int top) {
            entries = getEntries();
            //IF PAGE != 0, Display Left Button
            if (page != 0)
                book.addButton(new ArrowButton.Left(book, left + 20, top + 154, (button) -> back(book)));
            for (int i = 0; i < perPage/2; i++) {
                int id = (page * perPage) + i;
                if (id < entries.size())
                    initEntry(book, left + 2, top, i, entries.get(id));
            }
        }

        @Override
        public void initRight(Book book, int left, int top) {
            //IF PAGE != MAX_PAGE, Display Right Button
            int maxPage = Math.max(0, (int) Math.ceil(((double) entries.size())/perPage) - 1);
            if (page != maxPage)
                book.addButton(new ArrowButton.Right(book, left + 119, top + 154, (button) -> forward(book)));
            for (int i = perPage/2; i < perPage; i++) {
                int id = (page * perPage) + i;
                if (id < entries.size())
                    initEntry(book, left - 8, top, i - perPage/2, entries.get(id));
            }
        }
    }
}
