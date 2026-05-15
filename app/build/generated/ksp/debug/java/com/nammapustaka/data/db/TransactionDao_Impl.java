package com.nammapustaka.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nammapustaka.data.model.Book;
import com.nammapustaka.data.model.BorrowTransaction;
import com.nammapustaka.data.model.Student;
import com.nammapustaka.data.model.TransactionWithDetails;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BorrowTransaction> __insertionAdapterOfBorrowTransaction;

  private final EntityDeletionOrUpdateAdapter<BorrowTransaction> __updateAdapterOfBorrowTransaction;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBorrowTransaction = new EntityInsertionAdapter<BorrowTransaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `transactions` (`id`,`bookId`,`studentId`,`borrowDate`,`dueDate`,`returnDate`,`isReturned`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BorrowTransaction entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBookId());
        statement.bindLong(3, entity.getStudentId());
        statement.bindLong(4, entity.getBorrowDate());
        statement.bindLong(5, entity.getDueDate());
        if (entity.getReturnDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReturnDate());
        }
        final int _tmp = entity.isReturned() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__updateAdapterOfBorrowTransaction = new EntityDeletionOrUpdateAdapter<BorrowTransaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transactions` SET `id` = ?,`bookId` = ?,`studentId` = ?,`borrowDate` = ?,`dueDate` = ?,`returnDate` = ?,`isReturned` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BorrowTransaction entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBookId());
        statement.bindLong(3, entity.getStudentId());
        statement.bindLong(4, entity.getBorrowDate());
        statement.bindLong(5, entity.getDueDate());
        if (entity.getReturnDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getReturnDate());
        }
        final int _tmp = entity.isReturned() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertTransaction(final BorrowTransaction transaction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBorrowTransaction.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTransaction(final BorrowTransaction transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBorrowTransaction.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<TransactionWithDetails>> getActiveTransactions() {
    final String _sql = "SELECT * FROM transactions WHERE isReturned = 0 ORDER BY dueDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"books", "students",
        "transactions"}, true, new Callable<List<TransactionWithDetails>>() {
      @Override
      @Nullable
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
            final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
            final int _cursorIndexOfBorrowDate = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowDate");
            final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
            final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
            final int _cursorIndexOfIsReturned = CursorUtil.getColumnIndexOrThrow(_cursor, "isReturned");
            final LongSparseArray<Book> _collectionBook = new LongSparseArray<Book>();
            final LongSparseArray<Student> _collectionStudent = new LongSparseArray<Student>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfBookId);
              _collectionBook.put(_tmpKey, null);
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfStudentId);
              _collectionStudent.put(_tmpKey_1, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipbooksAscomNammapustakaDataModelBook(_collectionBook);
            __fetchRelationshipstudentsAscomNammapustakaDataModelStudent(_collectionStudent);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final BorrowTransaction _tmpTransaction;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpBookId;
              _tmpBookId = _cursor.getInt(_cursorIndexOfBookId);
              final int _tmpStudentId;
              _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
              final long _tmpBorrowDate;
              _tmpBorrowDate = _cursor.getLong(_cursorIndexOfBorrowDate);
              final long _tmpDueDate;
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
              final Long _tmpReturnDate;
              if (_cursor.isNull(_cursorIndexOfReturnDate)) {
                _tmpReturnDate = null;
              } else {
                _tmpReturnDate = _cursor.getLong(_cursorIndexOfReturnDate);
              }
              final boolean _tmpIsReturned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsReturned);
              _tmpIsReturned = _tmp != 0;
              _tmpTransaction = new BorrowTransaction(_tmpId,_tmpBookId,_tmpStudentId,_tmpBorrowDate,_tmpDueDate,_tmpReturnDate,_tmpIsReturned);
              final Book _tmpBook;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfBookId);
              _tmpBook = _collectionBook.get(_tmpKey_2);
              final Student _tmpStudent;
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfStudentId);
              _tmpStudent = _collectionStudent.get(_tmpKey_3);
              _item = new TransactionWithDetails(_tmpTransaction,_tmpBook,_tmpStudent);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<TransactionWithDetails>> getAllTransactions() {
    final String _sql = "SELECT * FROM transactions ORDER BY borrowDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"books", "students",
        "transactions"}, true, new Callable<List<TransactionWithDetails>>() {
      @Override
      @Nullable
      public List<TransactionWithDetails> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
            final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
            final int _cursorIndexOfBorrowDate = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowDate");
            final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
            final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
            final int _cursorIndexOfIsReturned = CursorUtil.getColumnIndexOrThrow(_cursor, "isReturned");
            final LongSparseArray<Book> _collectionBook = new LongSparseArray<Book>();
            final LongSparseArray<Student> _collectionStudent = new LongSparseArray<Student>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfBookId);
              _collectionBook.put(_tmpKey, null);
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfStudentId);
              _collectionStudent.put(_tmpKey_1, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipbooksAscomNammapustakaDataModelBook(_collectionBook);
            __fetchRelationshipstudentsAscomNammapustakaDataModelStudent(_collectionStudent);
            final List<TransactionWithDetails> _result = new ArrayList<TransactionWithDetails>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransactionWithDetails _item;
              final BorrowTransaction _tmpTransaction;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpBookId;
              _tmpBookId = _cursor.getInt(_cursorIndexOfBookId);
              final int _tmpStudentId;
              _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
              final long _tmpBorrowDate;
              _tmpBorrowDate = _cursor.getLong(_cursorIndexOfBorrowDate);
              final long _tmpDueDate;
              _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
              final Long _tmpReturnDate;
              if (_cursor.isNull(_cursorIndexOfReturnDate)) {
                _tmpReturnDate = null;
              } else {
                _tmpReturnDate = _cursor.getLong(_cursorIndexOfReturnDate);
              }
              final boolean _tmpIsReturned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsReturned);
              _tmpIsReturned = _tmp != 0;
              _tmpTransaction = new BorrowTransaction(_tmpId,_tmpBookId,_tmpStudentId,_tmpBorrowDate,_tmpDueDate,_tmpReturnDate,_tmpIsReturned);
              final Book _tmpBook;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfBookId);
              _tmpBook = _collectionBook.get(_tmpKey_2);
              final Student _tmpStudent;
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfStudentId);
              _tmpStudent = _collectionStudent.get(_tmpKey_3);
              _item = new TransactionWithDetails(_tmpTransaction,_tmpBook,_tmpStudent);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getActiveTransactionForBook(final int bookId,
      final Continuation<? super BorrowTransaction> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE bookId = ? AND isReturned = 0 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BorrowTransaction>() {
      @Override
      @Nullable
      public BorrowTransaction call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfBorrowDate = CursorUtil.getColumnIndexOrThrow(_cursor, "borrowDate");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfReturnDate = CursorUtil.getColumnIndexOrThrow(_cursor, "returnDate");
          final int _cursorIndexOfIsReturned = CursorUtil.getColumnIndexOrThrow(_cursor, "isReturned");
          final BorrowTransaction _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpBookId;
            _tmpBookId = _cursor.getInt(_cursorIndexOfBookId);
            final int _tmpStudentId;
            _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
            final long _tmpBorrowDate;
            _tmpBorrowDate = _cursor.getLong(_cursorIndexOfBorrowDate);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final Long _tmpReturnDate;
            if (_cursor.isNull(_cursorIndexOfReturnDate)) {
              _tmpReturnDate = null;
            } else {
              _tmpReturnDate = _cursor.getLong(_cursorIndexOfReturnDate);
            }
            final boolean _tmpIsReturned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsReturned);
            _tmpIsReturned = _tmp != 0;
            _result = new BorrowTransaction(_tmpId,_tmpBookId,_tmpStudentId,_tmpBorrowDate,_tmpDueDate,_tmpReturnDate,_tmpIsReturned);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipbooksAscomNammapustakaDataModelBook(
      @NonNull final LongSparseArray<Book> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipbooksAscomNammapustakaDataModelBook(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`title`,`author`,`category`,`totalPages`,`coverColor`,`qrCode`,`isAvailable` FROM `books` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfTitle = 1;
      final int _cursorIndexOfAuthor = 2;
      final int _cursorIndexOfCategory = 3;
      final int _cursorIndexOfTotalPages = 4;
      final int _cursorIndexOfCoverColor = 5;
      final int _cursorIndexOfQrCode = 6;
      final int _cursorIndexOfIsAvailable = 7;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Book _item_1;
          final int _tmpId;
          _tmpId = _cursor.getInt(_cursorIndexOfId);
          final String _tmpTitle;
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
          final String _tmpAuthor;
          _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
          final String _tmpCategory;
          _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
          final int _tmpTotalPages;
          _tmpTotalPages = _cursor.getInt(_cursorIndexOfTotalPages);
          final String _tmpCoverColor;
          _tmpCoverColor = _cursor.getString(_cursorIndexOfCoverColor);
          final String _tmpQrCode;
          _tmpQrCode = _cursor.getString(_cursorIndexOfQrCode);
          final boolean _tmpIsAvailable;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
          _tmpIsAvailable = _tmp != 0;
          _item_1 = new Book(_tmpId,_tmpTitle,_tmpAuthor,_tmpCategory,_tmpTotalPages,_tmpCoverColor,_tmpQrCode,_tmpIsAvailable);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipstudentsAscomNammapustakaDataModelStudent(
      @NonNull final LongSparseArray<Student> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipstudentsAscomNammapustakaDataModelStudent(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`name`,`className`,`totalPagesRead` FROM `students` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfClassName = 2;
      final int _cursorIndexOfTotalPagesRead = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final Student _item_1;
          final int _tmpId;
          _tmpId = _cursor.getInt(_cursorIndexOfId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final String _tmpClassName;
          _tmpClassName = _cursor.getString(_cursorIndexOfClassName);
          final int _tmpTotalPagesRead;
          _tmpTotalPagesRead = _cursor.getInt(_cursorIndexOfTotalPagesRead);
          _item_1 = new Student(_tmpId,_tmpName,_tmpClassName,_tmpTotalPagesRead);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
