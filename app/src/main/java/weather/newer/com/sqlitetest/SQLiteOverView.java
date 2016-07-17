package weather.newer.com.sqlitetest;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;

public class SQLiteOverView extends ListActivity {
    private MyAdapter dbHelper;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private Cursor cursor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getListView().setDividerHeight(2);
        dbHelper = new MyAdapter(this);
        try {
            dbHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fillData();
        registerForContextMenu(getListView());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listment, menu);
        return true;
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.insert:
                createTest();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.insert:
                createTest();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
                dbHelper.deleteTest(info.id);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void createTest()
    {
        Intent i = new Intent(this,Testdetails.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    // ListView 和 view (row) on 被点击
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, Testdetails.class);
        i.putExtra(MyAdapter.KEY_ROWID, id);
        // Activity返回一个结果给startActivityForResult
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    /**
     * 根据其他的Activity的结果被调用
     * requestCode是发送到Activity的原始码
     * resutCode是返回码，0表示一切OK
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();

    }

    private void fillData() {
        cursor = dbHelper.fetchALlTests();
        startManagingCursor(cursor);

        String[] from = new String[] { MyAdapter.KEY_SUMMARY };
        int[] to = new int[] { R.id.label };
        //现在创建一个数组适配器并且设置它现在在我们的列中
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
                R.layout.test_row, cursor, from, to);
        setListAdapter(notes);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}