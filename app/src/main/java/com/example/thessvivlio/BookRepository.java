package com.example.thessvivlio;

import android.os.AsyncTask;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class BookRepository {
    private BookDao bookDao;

    public BookRepository(Context context) {
        BookDatabase db = BookDatabase.getDatabase(context);
        bookDao = db.bookDao();
    }

    public void insert(BookEntity book) {
        new InsertAsyncTask(bookDao).execute(book);
    }

    public void delete(BookEntity book){
        new DeleteBookAsyncTask(bookDao).execute(book);
    }

    public void update(BookEntity book){
        new UpdateBookAsyncTask(bookDao).execute(book);
    }

    private static class InsertAsyncTask extends AsyncTask<BookEntity, Void, Void> {
        private BookDao asyncTaskDao;

        InsertAsyncTask(BookDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final BookEntity... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }

    }

    private static class DeleteBookAsyncTask extends AsyncTask<BookEntity, Void, Void> {
        private BookDao bookDao;

        public DeleteBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(BookEntity... books) {
            bookDao.delete(books[0]);
            return null;
        }
    }

    private class UpdateBookAsyncTask extends AsyncTask<BookEntity, Void, Void> {

        private BookDao bookDao;

        public UpdateBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(BookEntity... bookEntities) {
            if (bookEntities != null && bookEntities.length > 0) {
                bookDao.updateBook(bookEntities[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Handle post-execution tasks if needed
        }
    }
}
