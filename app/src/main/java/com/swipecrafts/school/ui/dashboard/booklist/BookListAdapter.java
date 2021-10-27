package com.swipecrafts.school.ui.dashboard.booklist;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private final ImageLoader<Book> mImageLoader;
    private List<Book> bookList;

    public BookListAdapter(List<Book> items, ImageLoader<Book> imageLoader) {
        bookList = items;
        mImageLoader = imageLoader;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.book = bookList.get(position);
        holder.bindView();

        if (!TextUtils.isEmpty(holder.book.getBookImgUrl())){
            holder.bookImage.setVisibility(View.VISIBLE);
            mImageLoader.loadImage(holder.bookImage, holder.book);
        }else holder.bookImage.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateBookList(List<Book> books) {
        this.bookList = books;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        private final TextView bookTitleTV;
        private final TextView bookPublisherTV;
        private final TextView bookWriterTV;
        private final TextView bookClassTV;
        private final TextView bookSubjectTV;
        private final TextView bookPriceTV;

        private final ImageView bookImage;

        public Book book;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            bookImage = (ImageView) view.findViewById(R.id.bookImageIV);

            bookTitleTV = (TextView) view.findViewById(R.id.bookTitleTV);
            bookPublisherTV = (TextView) view.findViewById(R.id.bookPublisherTV);
            bookWriterTV = (TextView) view.findViewById(R.id.bookWriterTV);
            bookClassTV = (TextView) view.findViewById(R.id.classNameTV);
            bookSubjectTV = (TextView) view.findViewById(R.id.subjectNameTV);
            bookPriceTV = (TextView) view.findViewById(R.id.bookPriceTV);

        }

        public void bindView() {
            bookTitleTV.setText(book.getBookTitle());
            bookPublisherTV.setText(book.getBookPub());
            bookWriterTV.setText(book.getBookWriter());
            bookClassTV.setText(book.getClassName());
            bookSubjectTV.setText(book.getSubjectName());
            bookPriceTV.setText(book.getBookPrice());
        }
    }
}
