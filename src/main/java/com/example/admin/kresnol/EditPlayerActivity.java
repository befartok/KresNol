package com.example.admin.kresnol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by admin on 10.11.18.
 */

public class EditPlayerActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    private Db db;
    private Cursor mCursor;
    private SimpleCursorAdapter mCursorAd;
    private ListView mLv;

    final int DIALOG = 1;

    Dialog dialog;
    int viewClicked;
    String playersName;

    EditText nameToDialog;

    RadioButton renamePlayer;
    RadioButton clearStats;
    RadioButton deletePlayer;
    long idOfRecord;

    static boolean updSpinner=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);

        //initView();

        db = new Db(this);

        mCursor = db.getAllItems();

        String[] from = new String[]{DbHelper.KEY_NAME, DbHelper.KEY_TOTAL_PLAY,
                DbHelper.KEY_TOTAL_WIN};

        //массив из полей шаблона item_edit_player
        int[] to = new int[]{R.id.edit_player_item_name, R.id.edit_player_item_totalPlay,
                R.id.edit_player_item_totalWin};

        //список игроков
        mLv = (ListView) findViewById(R.id.edit_player_lv);


        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG_TAG, "position = " + position + " && id = " + id);
                Log.i(LOG_TAG, "имя = " + db.getNameFromRecordOfDb(id));
                playersName = db.getNameFromRecordOfDb(id);
                removeDialog(DIALOG);
                showDialog(DIALOG);
                idOfRecord = id;

            }
        });

        mCursorAd = new SimpleCursorAdapter(this, R.layout.item_edit_player, mCursor, from, to, 0);

        mLv.setAdapter(mCursorAd);

    }


    public void onclick(View v) {
        viewClicked = v.getId();

        switch (viewClicked) {

            case R.id.dialog_rename:
                    Log.i(LOG_TAG, "переименовать");

                    nameToDialog.setEnabled(true);

               break;

            case R.id.dialog_clear_stats:
                    Log.i(LOG_TAG, "dialog_clear_stats");

                    nameToDialog.setEnabled(false);

               break;
            case R.id.dialog_delete_player:
                    Log.i(LOG_TAG, "delete_player");

                    nameToDialog.setEnabled(false);

               break;

            case R.id.dialog_button_cancel:
                dialog.dismiss();
                break;
            case R.id.dialog_button_ok:

                if (renamePlayer.isChecked()){

                    renamePlayer();
                }
                if (clearStats.isChecked()){

                    clearStats();
                }

                // TODO: 02.12.18 раскидать на MVP?
                if (deletePlayer.isChecked()){

                    deletePlayer();
                }

                break;
        }
    }

    private void renamePlayer() {
        Log.d(LOG_TAG, "rename");

        // проверка на уникальность имени
        if (db.checkName(nameToDialog.getText().toString())) {

            Toast toast= Toast.makeText(getBaseContext(),"Игрок с таким именем уже существует",Toast.LENGTH_LONG);
            //Выставляем положение сообщения вверху экрана:
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();

        } else {
            db.editName(idOfRecord, nameToDialog.getText().toString());

            updSpinner = true;

            closeDialog();

        }
    }

    private void clearStats() {
        Log.d(LOG_TAG, "clearStats");

        db.clearStats(idOfRecord);

        closeDialog();
    }

    private void deletePlayer() {
        Log.d(LOG_TAG, "deletePlayer");

        // запретить удалять имена по умолчанию
        if (playersName.equals(getResources().getString(R.string.droids_name))
                |(playersName.equals(getResources().getString(R.string.players1_name)))
                |(playersName.equals(getResources().getString(R.string.players2_name))))
        {
            Toast.makeText(getBaseContext(),"Стандартного игрока "+playersName+" удалять нельзя",
                    Toast.LENGTH_SHORT).show();
        }else {

            db.deleteItem(idOfRecord);
            updSpinner = true;

            closeDialog();
        }
    }

    public static boolean isUpdSpinner() {
        return updSpinner;
    }

    public void closeDialog() {
        mCursor = db.getAllItems();
        mCursorAd.changeCursor(mCursor); // обновить список
        removeDialog(DIALOG);

    }




        @Override
    protected Dialog onCreateDialog(int id) {

        // создаем view из dialog_edit_player.xml
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_edit_player,
                null);

        nameToDialog = (EditText) layout.findViewById(R.id.dialog_edit_player_item_name);

        nameToDialog.setEnabled(false);
        nameToDialog.setTextColor(getResources().getColor(R.color.colorBlack));

        renamePlayer = (RadioButton) layout.findViewById(R.id.dialog_rename);
        clearStats = (RadioButton) layout.findViewById(R.id.dialog_clear_stats);
        deletePlayer = (RadioButton) layout.findViewById(R.id.dialog_delete_player);

        nameToDialog.setText(playersName);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(layout);

        dialog = builder.create();
        return dialog;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение
        db.close();
    }

}
