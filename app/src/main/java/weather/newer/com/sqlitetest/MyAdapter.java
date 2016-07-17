package weather.newer.com.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by windows on 2016/7/17.
 */
public class MyAdapter
{public static final String KEY_ROWID = "_id";
public static final String KEY_CATEGORY = "category";
public static final String KEY_SUMMARY = "summary";
public static final String KEY_DESCRIPTION = "description";
public static final String DATABASE_TABLE = "test";
private Context context;
private SQLiteDatabase db;
private MySQLite  dbHelper;

        public MyAdapter(Context context)
        {
            this.context = context;
        }

        public MyAdapter open() throws SQLException
        {
            dbHelper = new MySQLite(context);
            db = dbHelper.getWritableDatabase();
            return this;
        }

        public void close()
        {
            dbHelper.close();
        }
        /**
         * 创建一个新的标test，如果test成功创建则返回一个新的列id，否则返回-1来表示失败
         */
        public long createTest(String category, String summary, String description)
        {
            ContentValues initialValues = createContentValues(category, summary, description);
            return db.insert(DATABASE_TABLE, null, initialValues);
        }
        //更新表test
        public boolean updateTest(long rowId, String category, String summary, String description)
        {
            ContentValues updateValues = createContentValues(category, summary, description);
            return db.update(DATABASE_TABLE, updateValues, KEY_ROWID + "=" +rowId, null)>0;
        }
        //删除表test
        public boolean deleteTest(long rowId)
        {
            return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null)>0;
        }
        //返回数据库中表test的所有游标列表
        public Cursor fetchALlTests()
        {
            return db.query(DATABASE_TABLE, new String[]{KEY_ROWID,
                    KEY_CATEGORY,KEY_SUMMARY,KEY_DESCRIPTION}, null, null, null, null, null);
        }
        //返回在表test中被定义的某个位置的游标
        public Cursor fetchTest(long rowId)throws SQLException
        {
            Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
                            KEY_CATEGORY, KEY_SUMMARY, KEY_DESCRIPTION},
                    KEY_ROWID + "=" +rowId, null, null, null,null,null);
            if(mCursor != null)
            {
                mCursor.moveToFirst();
            }
            return mCursor;
        }

        private ContentValues createContentValues(String category, String summary,
                                                  String description) {
            ContentValues values = new ContentValues();
            values.put(KEY_CATEGORY, category);
            values.put(KEY_SUMMARY, summary);
            values.put(KEY_DESCRIPTION, description);
            return values;
        }
}