package com.example.admin.kresnol;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


//import static com.example.admin.kresnol.R.id.group1;


/**
 * Created by admin on 26.07.18.
 */

public class MainPresenter {
    private MainActivity view;
    private final MainModel model;
    private final LogicOfDroid logic;

    Player leftPlayer;
    Player rightPlayer;

    Db db;
    Context context;

    final String LOG_TAG = "myLogs";

    boolean equalsPlayersLeft = false;
    boolean equalsPlayers = false;


    //константы для запоминания в настройках выбора спинеров
    final String LASTLEFTSPINN = "lastLeftSpinn";
    final String LASTRIGHTSPINN = "lastRightSpinn";
    final String LASTLEVELSPINN = "lastLevelSpinn";


    public MainPresenter(MainActivity mainActivity) {
        view = mainActivity;

        //в модель передаю уровни игры из ресурсов
        model = new MainModel(view.getResources().getString(R.string.level_easy),
                view.getResources().getString(R.string.level_normal),
                view.getResources().getString(R.string.level_hard));

        logic = new LogicOfDroid(view.getResources());


        // брать имена игроков из базы для создания объектов игрок
        db = new Db(view);

        leftPlayer = new Player(db.getNameForCreateLeftPlayer());
        rightPlayer = new Player(db.getNameForCreateRightPlayer());

        db.close();

        leftPlayer.setSymbol("x");
        leftPlayer.setActive(true);

        rightPlayer.setSymbol("o");
        rightPlayer.setActive(false);

        getArrayOfPlayer();
        getArrayOfPlayerForLeft();

    }

    List<String> arrayOfPlayer = new ArrayList<String>();

    //public String[] getArrayOfPlayer() {
    public List<RecordOfDb> getArrayOfPlayer() {
        db = new Db(view);

        List<RecordOfDb> records = db.getNamesFromDb();
        for (RecordOfDb record : records) {
            arrayOfPlayer.add(record.getName());

            //Log.d(LOG_TAG, "Имя: " + record.getName() + " сыграно: " + record.getTotalPlay());
            Log.d(LOG_TAG, "Имя: " + record.getName());
        }
        //return model.arrayOfPlayers;
        db.close();
        return records;
    }

    List<String> arrayOfPlayerForLeft = new ArrayList<String>();

    //создание массива для спинера левого без игрока Андроид
    public List<RecordOfDb> getArrayOfPlayerForLeft() {
        db = new Db(view);

        List<RecordOfDb> records = db.getNamesFromDb();
        for (RecordOfDb record : records) {

            if (!record.getName().equals(view.getResources().getString(R.string.droids_name))) {
                arrayOfPlayerForLeft.add(record.getName());

                //Log.d(LOG_TAG, "Имя: " + record.getName() + " сыграно: " + record.getTotalPlay());
                Log.d(LOG_TAG, "Имя1: " + record.getName());
            }

        }
        //return model.arrayOfPlayers;
        db.close();
        return records;
    }

    //обновление массивов игроков и адаптеров спинеров после создания новых игроков
    public void updateSpinner() {
        //boolean updSpin = false;
        if (CreatePlayerActivity.isUpdateSpinner() | EditPlayerActivity.isUpdSpinner()) {
            arrayOfPlayer.clear();
            arrayOfPlayerForLeft.clear();
            getArrayOfPlayer();
            getArrayOfPlayerForLeft();
            view.updateAdapters();

            //updSpin = true;
        }
        //return updSpin;
    }

 /*
    //обновление массивов игроков и адаптеров спинеров после создания новых игроков
    public boolean updateSpinner() {
        boolean updSpin = false;
        if (CreatePlayerActivity.isUpdateSpinner() | EditPlayerActivity.isUpdSpinner()) {
            arrayOfPlayer.clear();
            arrayOfPlayerForLeft.clear();
            getArrayOfPlayer();
            getArrayOfPlayerForLeft();
            view.updateAdapters();

            updSpin = true;
        }
        return updSpin;
    }

 */

    // TODO: 25.01.19 проверить необходимость булеан
    public void setSpinnerToNewPlayer() {
        //boolean setSpinnerToNewPlayer = false;
        if (CreatePlayerActivity.isSetPlayerToGame()) {
            //int position = view.adapter.getPosition(CreatePlayerActivity.getNamePlayerToSet());

            if (CreatePlayerActivity.getPositionToSet().equals("left")) {
                int positionForLeft = view.adapterForLeft.getPosition(CreatePlayerActivity.getNamePlayerToSet());
                view.spinnerLeft.setSelection(positionForLeft);
                setSpinnerLeft(view.spinnerLeft.getSelectedItem().toString());

            }

            if (CreatePlayerActivity.getPositionToSet().equals("right")) {
                int position = view.adapter.getPosition(CreatePlayerActivity.getNamePlayerToSet());
                view.spinnerRight.setSelection(position);
                setSpinnerRight(view.spinnerRight.getSelectedItem().toString());


            }


            //setSpinnerToNewPlayer = true;
        }
        //return setSpinnerToNewPlayer;
    }

    public void setSpinnerToOldPlayer() {

        view.spinnerLeft.setSelection(0, true);
        //view.spinnerLeft.setSelection(0);
        setSpinnerLeft(view.spinnerLeft.getSelectedItem().toString());

        //view.spinnerRight.setSelection(1);
        view.spinnerRight.setSelection(1, true);
        setSpinnerRight(view.spinnerRight.getSelectedItem().toString());


    }



/*

 // TODO: 25.01.19 проверить необходимость булеан
    public boolean setSpinnerToNewPlayer() {
        boolean setSpinnerToNewPlayer = false;
        if (CreatePlayerActivity.isSetPlayerToGame()) {
            //int position = view.adapter.getPosition(CreatePlayerActivity.getNamePlayerToSet());

            if (CreatePlayerActivity.getPositionToSet().equals("left")) {
                int positionForLeft = view.adapterForLeft.getPosition(CreatePlayerActivity.getNamePlayerToSet());
                view.spinnerLeft.setSelection(positionForLeft);
                setSpinnerLeft(view.spinnerLeft.getSelectedItem().toString());

            }

            if (CreatePlayerActivity.getPositionToSet().equals("right")) {
                int position = view.adapter.getPosition(CreatePlayerActivity.getNamePlayerToSet());
                view.spinnerRight.setSelection(position);
                setSpinnerRight(view.spinnerRight.getSelectedItem().toString());

            }


            setSpinnerToNewPlayer = true;
        }
        return setSpinnerToNewPlayer;
    }
*/


    public String[] getArrayOfLevel() {
        return model.arrayOfLevel;
    }


    public void setSpinnersFromPreferences() {



        String lastSpinLeft = view.prefs.getString(LASTLEFTSPINN, "");
        String lastSpinRight = view.prefs.getString(LASTRIGHTSPINN, "");
        String lastSpinLevel = view.prefs.getString(LASTLEVELSPINN, "");

        //view.adapterForLeft.getPosition(lastSpinLeft);




        //проверяем, если удалялись игроки, из настроек для запуска, то устанавливаем спинеры по умолчанию
        if ((view.adapter.getPosition(lastSpinRight)<0)
                |(view.adapterForLeft.getPosition(lastSpinLeft)<0)
                |view.adapterLevel.getPosition(lastSpinLevel)<0){
            view.spinnerLeft.setSelection(0);
            setSpinnerLeft(view.spinnerLeft.getSelectedItem().toString());
            view.spinnerRight.setSelection(1);
            setSpinnerRight(view.spinnerRight.getSelectedItem().toString());
            Toast.makeText(view.getBaseContext(), "установлены начальные игроки", Toast.LENGTH_SHORT).show();

        }
        //иначе устанавливаем спинеры из настроек
        else {
            int positionLastSpinLeft = view.adapterForLeft.getPosition(lastSpinLeft);
            view.spinnerLeft.setSelection(positionLastSpinLeft);
            setSpinnerLeft(view.spinnerLeft.getSelectedItem().toString());

            int positionLastSpinRight = view.adapter.getPosition(lastSpinRight);
            view.spinnerRight.setSelection(positionLastSpinRight);
            setSpinnerRight(view.spinnerRight.getSelectedItem().toString());

            if (view.spinnerRight.getSelectedItem().toString().equals(view.getResources().getString(R.string.droids_name))) {
                int positionLastSpinLevel = view.adapterLevel.getPosition(lastSpinLevel);
                view.spinnerLevel.setSelection(positionLastSpinLevel);
                setSpinnerLevel(view.spinnerLevel.getSelectedItem().toString());

            }
        }

    }


    public void setSpinnerLeft(String spinLeft) {

        model.setSpinnerLeftValue(spinLeft);
        leftPlayer.setName(spinLeft);
        Log.d(LOG_TAG, "левый игрок = " + leftPlayer.getName());
    }

    public void setSpinnerRight(String spinRight) {
        model.setSpinnerRightValue(spinRight);
        rightPlayer.setName(spinRight);

        Log.d(LOG_TAG, "правый игрок = " + rightPlayer.getName());

        setImageRight();
    }

    public String getSpinnerLeft() {
        return model.getSpinnerLeftValue();
    }

    public String getSpinnerRight() {
        return model.getSpinnerRightValue();
    }

    public void setImageRight() {
        if (model.getSpinnerRightValue().equals(view.getResources().getString(R.string.droids_name))) {
            if (rightPlayer.isActive()) {
                view.imageOfRightPlayer.setImageResource(R.drawable.ic_android_black_24dp);

            } else view.imageOfRightPlayer.setImageResource(R.drawable.ic_android_gray_24dp);
        } else if (rightPlayer.isActive()) {
            view.imageOfRightPlayer.setImageResource(R.drawable.ic_accessibility_black_24dp);

        } else view.imageOfRightPlayer.setImageResource(R.drawable.ic_human_gray_24dp);


    }

    public void setSpinnerLevel(String spinLevel) {

        model.setSpinnerLevelValue(spinLevel);
    }

    public void checkVisibilitySpinnerLevel() {

        if (model.getSpinnerRightValue().equals(view.getResources().getString(R.string.droids_name))) {


            view.spinnerLevel.setVisibility(View.VISIBLE);
        } else view.spinnerLevel.setVisibility(View.INVISIBLE);
    }

    public void click(Integer id) {
        switch (id) {
            case R.id.buttonSymbolRightPlayer:
            case R.id.buttonSymbolLeftPlayer:
                Log.d(LOG_TAG, "92 click test");


                //if (model.statusGames == "ready") {
                if (model.getStatusGames().equals(view.getResources().getString(R.string.statusGamesReady))) {

                    // меняет символ
                    invertVariables();

                    //первый выбор символа, установка активности
                    if ((model.clickedButtonsTotal == 0) & (model.numOfRestart == 0)) {
                        //Log.d(LOG_TAG, "128 clickedButtonsTotal=" + clickedButtonsTotal);
                        if (leftPlayer.getSymbol().equals("x")) {
                            leftPlayer.setActive(true);

                            //makeNameActive("leftButton");
                            makeNameActive("leftName");
                            rightPlayer.setActive(false);
                        } else if (leftPlayer.getSymbol().equals("o")) {
                            leftPlayer.setActive(false);
                            //makeNameActive("rightButton");
                            makeNameActive("rightName");
                            rightPlayer.setActive(true);
                        }
                    }
                }
                break;

            //перезапуск
            case R.id.layout0:

                //if (model.statusGames == "finish") {
                if (model.getStatusGames().equals(view.getResources().getString(R.string.statusGamesFinish))) {
                    restartGame();
                }
                break;

            //нажатие кнопки игроком в игровом поле
            case R.id.button0:
                clickPlayFieldBtn(view.arrayOfButtons[0]);
                break;
            case R.id.button1:
                clickPlayFieldBtn(view.arrayOfButtons[1]);
                break;
            case R.id.button2:
                clickPlayFieldBtn(view.arrayOfButtons[2]);
                break;
            case R.id.button3:
                clickPlayFieldBtn(view.arrayOfButtons[3]);
                break;
            case R.id.button4:
                clickPlayFieldBtn(view.arrayOfButtons[4]);
                break;
            case R.id.button5:
                clickPlayFieldBtn(view.arrayOfButtons[5]);
                break;
            case R.id.button6:
                clickPlayFieldBtn(view.arrayOfButtons[6]);
                break;
            case R.id.button7:
                clickPlayFieldBtn(view.arrayOfButtons[7]);
                break;
            case R.id.button8:
                clickPlayFieldBtn(view.arrayOfButtons[8]);
                break;
        }
    }

    public boolean clickMenu(Integer idMenu) {

        Log.d(LOG_TAG, "clickMenu test ");

        switch (idMenu) {

            case R.id.menu_settings:
                view.setPrefs();


                return true;


            case R.id.menu_records:
                view.menuHighScore();
                return true;
            case R.id.menu_about:
                view.menuAbout();
                return true;

            default:
                return false;
        }


    }

    // TODO: 04.10.18 доделать меню
    public void menuCreatePlayer(Menu myMenu) {

    }

    public void menuEditPlayer(Menu myMenu) {

    }

    public void menuDeletePlayer(Menu myMenu) {

    }

    public void menuLevel(Menu myMenu) {

    }

    public void menuRecords(Menu myMenu) {

    }

    public void menuAbout(Menu myMenu) {

    }

    //меняем символ у игроков
    public void invertVariables() {
        String bufferVariable;

        bufferVariable = leftPlayer.getSymbol();
        leftPlayer.setSymbol(rightPlayer.getSymbol());
        rightPlayer.setSymbol(bufferVariable);

        view.symbolOfBtnLeftPlayer.setText(leftPlayer.getSymbol());
        view.symbolOfBtnRightPlayer.setText(rightPlayer.getSymbol());

    }

    // TODO: 05.02.19 убрать нэйм из названия
    public void makeNameActive(String selectedSymbolsButton) {

        stopOfAnimation();

        switch (selectedSymbolsButton) {

            //case "leftButton":
            case "leftName":
                //((TextView) view.spinnerLeft.getSelectedView()).setTextColor(view.getResources().getColor(R.color.buttonsTextActive));
                //((TextView) view.spinnerLeft.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
                //((TextView) view.spinnerRight.getSelectedView()).setTextColor(view.getResources().getColor(R.color.buttonsText));
                //((TextView) view.spinnerRight.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                view.imageOfLeftPlayer.startAnimation(view.animation);

                view.imageOfLeftPlayer.setImageResource(R.drawable.ic_accessibility_black_24dp);

                if (model.getSpinnerRightValue().equals(view.getResources().getString(R.string.droids_name))) {

                    view.imageOfRightPlayer.setImageResource(R.drawable.ic_android_gray_24dp);
                } else

                    view.imageOfRightPlayer.setImageResource(R.drawable.ic_human_gray_24dp);

                break;

            //case "rightButton":
            case "rightName":
                //view.imageOfLeftPlayer.clearAnimation();
               // ((TextView) view.spinnerLeft.getSelectedView()).setTextColor(view.getResources().getColor(R.color.buttonsText));
                //((TextView) view.spinnerLeft.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);

                //((TextView) view.spinnerRight.getSelectedView()).setTextColor(view.getResources().getColor(R.color.buttonsTextActive));
                //((TextView) view.spinnerRight.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
                view.imageOfRightPlayer.startAnimation(view.animation);

                if (model.getSpinnerRightValue().equals(view.getResources().getString(R.string.droids_name))) {
                    view.imageOfRightPlayer.setImageResource(R.drawable.ic_android_black_24dp);
                } else {
                    view.imageOfRightPlayer.setImageResource(R.drawable.ic_accessibility_black_24dp);
                }
                view.imageOfLeftPlayer.setImageResource(R.drawable.ic_human_gray_24dp);

                break;
        }
    }

    private void restartGame() {

        Log.d(view.LOG_TAG, "258 restart");
        model.numOfRestart++;

        model.clickedButtonsTotal = 0;

        for (int i = 0; i < 9; i++) {
            view.arrayOfButtons[i].setTextColor(Color.BLACK);
            view.arrayOfButtons[i].setText("");
        }

        model.setStatusGames(view.getResources().getString(R.string.statusGamesReady));

        // TODO: 19.10.18 подумать над брать игрока не из спинера а из гетера модели игрока

        enableChangeSymbol();
        invertPlayersActivity();

        if ((view.spinnerRight.getSelectedItem().toString().equals(view.getResources().getString(R.string.droids_name))) & rightPlayer.isActive()) {

            clickPlayFieldBtn(view.arrayOfButtons[logic.droidsStep(view.arrayOfButtons, leftPlayer, rightPlayer, model)]);
        }
    }

    //меняем активность игроков
    public void invertPlayersActivity() {
        Boolean tempActive;

        tempActive = leftPlayer.isActive();
        leftPlayer.setActive(rightPlayer.isActive());
        rightPlayer.setActive(tempActive);

        if (leftPlayer.isActive()) {
            //makeNameActive("leftButton");
            makeNameActive("leftName");
        } else if (rightPlayer.isActive()) {
            //makeNameActive("rightButton");
            makeNameActive("rightName");
        }
    }

    public void clickPlayFieldBtn(SquareButton btn) {

        saveSpinners();

        // проверка нажатости кнопки и закончившесяй игры
        if ((!model.getStatusGames().equals(view.getResources().getString(R.string.statusGamesFinish)))
                & (btn.getText().equals(""))) {

            model.setStatusGames(view.getResources().getString(R.string.statusGamesInplay));

            //отключение изменяемости кнопок выбора символа и игроков
            disableChangeSymbol();

            if (leftPlayer.isActive()) {
                btn.setText(leftPlayer.getSymbol());
            } else if (rightPlayer.isActive()) {
                btn.setText(rightPlayer.getSymbol());
            }

            //увеличить счетчик нажатых кнопок
            model.clickedButtonsTotal++;
            // плюсовать в БД игры для первого нажатия кнопки
            if (model.clickedButtonsTotal == 1) {
                addGameToDb();
            }
            // проверка на выигрыш
            checkWin();

            //проверка числа нажатых кнопок
            if (model.clickedButtonsTotal == 9) {
                //model.statusGames = "finish";
                model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));
                stopOfAnimation();

            }

            //передача ход 2му игроку если андроид, то ход по алгоритму, иначе обрабатывать нажатие

            if ((model.getSpinnerRightValue().equals(view.getResources().getString(R.string.droids_name))) & (leftPlayer.isActive())
                    & (model.getStatusGames().equals(view.getResources().getString(R.string.statusGamesInplay)))) {
                invertPlayersActivity();

                clickPlayFieldBtn(view.arrayOfButtons[logic.droidsStep(view.arrayOfButtons, leftPlayer, rightPlayer, model)]);
            }
            //else if (model.statusGames == "inplay") {
            else if (model.getStatusGames().equals(view.getResources().getString(R.string.statusGamesInplay))) {
                invertPlayersActivity();
            }

        }
        //else if (model.statusGames == "finish") {
        else if (model.getStatusGames().equals(view.getResources().getString(R.string.statusGamesFinish))) {
            restartGame();
        }
    }

    // TODO: 12.01.19 вынести префс из вью в презентер
    // запоминаем в настройках спинеры
    public void saveSpinners() {
        SharedPreferences.Editor ed = view.prefs.edit();
        ed.putString(LASTLEFTSPINN, view.spinnerLeft.getSelectedItem().toString());
        ed.putString(LASTRIGHTSPINN, view.spinnerRight.getSelectedItem().toString());

        if (view.spinnerRight.getSelectedItem().toString().equals(view.getResources().getString(R.string.droids_name))) {
            ed.putString(LASTLEVELSPINN, view.spinnerLevel.getSelectedItem().toString());

        }
        ed.commit();
    }

    //добавление запущенной игры в базу
    private void addGameToDb() {

        db = new Db(view);
        /*leftPlayer.getName();
        rightPlayer.getName();*/

        Log.d(LOG_TAG, "leftPlayer = " + leftPlayer.getName());
        Log.d(LOG_TAG, "rightPlayer = " + rightPlayer.getName());

        db.addGame(leftPlayer.getName(), rightPlayer.getName());
        //db.addGame("Player1", "Player2");

        db.close();
    }

    //отключение возможности менять игровые символы
    public void disableChangeSymbol() {
        view.symbolOfBtnLeftPlayer.setEnabled(false);
        view.symbolOfBtnRightPlayer.setEnabled(false);
        view.spinnerLeft.setEnabled(false);
        view.spinnerRight.setEnabled(false);
        view.spinnerLevel.setEnabled(false);
        setMenuEnabled(false);

    }

    //включение возможности менять игровые символы
    public void enableChangeSymbol() {
        view.symbolOfBtnLeftPlayer.setEnabled(true);
        view.symbolOfBtnRightPlayer.setEnabled(true);
        view.spinnerLeft.setEnabled(true);
        view.spinnerRight.setEnabled(true);
        view.spinnerLevel.setEnabled(true);
        setMenuEnabled(true);
    }

    private void setMenuEnabled(boolean enabled) {

        view.myMenu.setGroupEnabled(R.id.group1, enabled);

    }

    public void checkWin() {

        String symbolActive;
        if (leftPlayer.isActive()) {
            symbolActive = leftPlayer.getSymbol();
        } else symbolActive = rightPlayer.getSymbol();

        if ((view.arrayOfButtons[0].getText().equals(symbolActive))
                & (view.arrayOfButtons[1].getText().equals(symbolActive))
                & (view.arrayOfButtons[2].getText().equals(symbolActive))) {


            //model.statusGames = "finish";
            model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));
            view.arrayOfButtons[0].setTextColor(Color.RED);
            view.arrayOfButtons[1].setTextColor(Color.RED);
            view.arrayOfButtons[2].setTextColor(Color.RED);

            saveResult(view.arrayOfButtons[0].getText());
        }

        if ((view.arrayOfButtons[3].getText().equals(symbolActive))
                & (view.arrayOfButtons[4].getText().equals(symbolActive))
                & (view.arrayOfButtons[5].getText().equals(symbolActive))) {

            //model.statusGames = "finish";
            model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));

            view.arrayOfButtons[3].setTextColor(Color.RED);
            view.arrayOfButtons[4].setTextColor(Color.RED);
            view.arrayOfButtons[5].setTextColor(Color.RED);

            saveResult(view.arrayOfButtons[3].getText());
        }

        if ((view.arrayOfButtons[6].getText().equals(symbolActive))
                & (view.arrayOfButtons[7].getText().equals(symbolActive))
                & (view.arrayOfButtons[8].getText().equals(symbolActive))) {

            //model.statusGames = "finish";
            model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));

            view.arrayOfButtons[6].setTextColor(Color.RED);
            view.arrayOfButtons[7].setTextColor(Color.RED);
            view.arrayOfButtons[8].setTextColor(Color.RED);

            saveResult(view.arrayOfButtons[6].getText());

        }
        if ((view.arrayOfButtons[0].getText().equals(symbolActive))
                & (view.arrayOfButtons[3].getText().equals(symbolActive))
                & (view.arrayOfButtons[6].getText().equals(symbolActive))) {
            //model.statusGames = "finish";
            model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));

            view.arrayOfButtons[0].setTextColor(Color.RED);
            view.arrayOfButtons[3].setTextColor(Color.RED);
            view.arrayOfButtons[6].setTextColor(Color.RED);

            saveResult(view.arrayOfButtons[0].getText());
        }

        if ((view.arrayOfButtons[1].getText().equals(symbolActive))
                & (view.arrayOfButtons[4].getText().equals(symbolActive))
                & (view.arrayOfButtons[7].getText().equals(symbolActive))) {
            //model.statusGames = "finish";
            model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));

            view.arrayOfButtons[1].setTextColor(Color.RED);
            view.arrayOfButtons[4].setTextColor(Color.RED);
            view.arrayOfButtons[7].setTextColor(Color.RED);

            saveResult(view.arrayOfButtons[1].getText());
        }
        if ((view.arrayOfButtons[2].getText().equals(symbolActive))
                & (view.arrayOfButtons[5].getText().equals(symbolActive))
                & (view.arrayOfButtons[8].getText().equals(symbolActive))) {
            //model.statusGames = "finish";
            model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));

            view.arrayOfButtons[2].setTextColor(Color.RED);
            view.arrayOfButtons[5].setTextColor(Color.RED);
            view.arrayOfButtons[8].setTextColor(Color.RED);

            saveResult(view.arrayOfButtons[2].getText());
        }
        if ((view.arrayOfButtons[0].getText().equals(symbolActive))
                & (view.arrayOfButtons[4].getText().equals(symbolActive))
                & (view.arrayOfButtons[8].getText().equals(symbolActive))) {
            //model.statusGames = "finish";
            model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));

            view.arrayOfButtons[0].setTextColor(Color.RED);
            view.arrayOfButtons[4].setTextColor(Color.RED);
            view.arrayOfButtons[8].setTextColor(Color.RED);

            saveResult(view.arrayOfButtons[0].getText());
        }
        if ((view.arrayOfButtons[2].getText().equals(symbolActive))
                & (view.arrayOfButtons[4].getText().equals(symbolActive))
                & (view.arrayOfButtons[6].getText().equals(symbolActive))) {

            //model.statusGames = "finish";
            model.setStatusGames(view.getResources().getString(R.string.statusGamesFinish));

            view.arrayOfButtons[2].setTextColor(Color.RED);
            view.arrayOfButtons[4].setTextColor(Color.RED);
            view.arrayOfButtons[6].setTextColor(Color.RED);

            saveResult(view.arrayOfButtons[2].getText());
        }
    }


    public void saveResult(CharSequence winSymbol) {

        stopOfAnimation();
        db = new Db(view);

        if (view.symbolOfBtnLeftPlayer.getText().equals(winSymbol)) {

            model.totalWinLeft++;

            db = new Db(view);
            // добавлять в бд выигрыш
            db.addWinToDb(leftPlayer.getName());


            view.winLeft.setText(Integer.toString(model.totalWinLeft));


        } else if (view.symbolOfBtnRightPlayer.getText().equals(winSymbol)) {

            model.totalWinRight++;
            //  добавлять в бд выигрыш
            db.addWinToDb(rightPlayer.getName());

            view.winRight.setText(Integer.toString(model.totalWinRight));
        }
        db.close();
    }

    public void stopOfAnimation() {
        view.imageOfLeftPlayer.clearAnimation();
        view.imageOfRightPlayer.clearAnimation();
    }

    //при смене игроков обнулять счет
    public void clearScore() {
        model.totalWinLeft = 0;
        view.winLeft.setText(Integer.toString(model.totalWinLeft));

        model.totalWinRight = 0;
        view.winRight.setText(Integer.toString(model.totalWinRight));
    }


    public void setSpinnerLevelFromPreferences() {
        String prefLevel = view.prefs.getString("pref_level", "");

        //winLeft.setText(prefLevel);
        Log.d(LOG_TAG, "Уровень сложности установленный в настройках" + prefLevel);

        setSpinnerLevel(prefLevel);


        switch (prefLevel) {
            case "Easy":
                view.spinnerLevel.setSelection(0);
                break;

            case "Normal":
                view.spinnerLevel.setSelection(1);

                break;
            case "Hard":
                view.spinnerLevel.setSelection(2);
                break;
        }
    }

    public void checkEqualsSpinnerLeft() {
        if (view.spinnerLeft.getSelectedItem().toString().equals(getSpinnerRight())) {
            Toast.makeText(view.getBaseContext(), "нельзя выбрать одного игрока ", Toast.LENGTH_SHORT).show();

            equalsPlayersLeft = true;

            int positionCurrentSpinLeft = view.adapterForLeft.getPosition(getSpinnerLeft());
            view.spinnerLeft.setSelection(positionCurrentSpinLeft);

        } else {
            setSpinnerLeft(view.spinnerLeft.getSelectedItem().toString());

            //сброс счетчика счета
            if (equalsPlayersLeft == false) {
                clearScore();
            }
            equalsPlayersLeft = false;
        }
    }

    // TODO: 29.01.19 исправить при удалении игроков проверка срабатывает, но устанавливает одинаковых если слева был п2, но при удалении правого ставится п2 по умолчанию

    //проверка и запрет выбора одного игрока в обоих спинерах
    public void checkEqualsSpinnerRight() {
        if (view.spinnerRight.getSelectedItem().toString().equals(getSpinnerLeft())) {

            Toast.makeText(view.getBaseContext(), "нельзя выбрать одного игрока ", Toast.LENGTH_SHORT).show();
            equalsPlayers = true;

            int positionCurrentSpinRight = view.adapter.getPosition(getSpinnerRight());
            view.spinnerRight.setSelection(positionCurrentSpinRight);
        } else {

            setSpinnerRight(view.spinnerRight.getSelectedItem().toString());
            Log.d(LOG_TAG, "846");

            //сброс счетчика счета
            if (equalsPlayers == false) {
                clearScore();
            }
            equalsPlayers = false;

            //видимость спинера уровня для игрока Андроид
            checkVisibilitySpinnerLevel();
        }
    }

    // TODO: 05.02.19 не получается установить второй спинер автоматом. после теста удалить
    public void setSpinnerColor(Player player, AdapterView<?> parent) {

        //устанавливаем цвет спинера при выборе спинера
        if (player.isActive()) {
            //((TextView) parent.getChildAt(0)).setTextColor(view.getResources().getColor(R.color.buttonsTextActive));
            ((TextView) parent.getChildAt(0)).setTextColor(view.getResources().getColor(R.color.colorPrimary));
        } else
            //((TextView) parent.getChildAt(0)).setTextColor(view.getResources().getColor(R.color.buttonsText));
            ((TextView) parent.getChildAt(0)).setTextColor(view.getResources().getColor(R.color.colorAccent));

    }


    public void setSpinnerColor() {

        Log.d(LOG_TAG, " leftPlayer.isActive()= " + leftPlayer.isActive());
        Log.d(LOG_TAG, " rightPlayer.isActive()= " + rightPlayer.isActive());

        //устанавливаем цвет спинера при выборе спинера
        if (leftPlayer.isActive()) {
            Log.d(LOG_TAG, " leftPlayer.isActive()= " + leftPlayer.isActive());

            ((TextView) view.spinnerLeft.getSelectedView()).setTextColor(Color.BLUE);
            ((TextView) view.spinnerRight.getSelectedView()).setTextColor(Color.MAGENTA);

            Log.d(LOG_TAG, " ((TextView) view.spinnerLeft.getSelectedView()).getTextColors()= " + ((TextView) view.spinnerLeft.getSelectedView()).getTextColors());
            //Log.d(LOG_TAG, " ((TextView) view.spinnerRight.getSelectedView()).getTextColors()= " + view.spinnerRight.getSelectedItem().toString());

            //((TextView) view.spinnerLeft.getSelectedView()).setTextColor(view.getResources().getColor(R.color.buttonsTextActive));
            //((TextView) view.spinnerRight.getSelectedView()).setTextColor(Color.CYAN);

        } else if (rightPlayer.isActive()) {
            Log.d(LOG_TAG, " rightPlayer.isActive()= " + rightPlayer.isActive());

            // ((TextView) view.spinnerRight.getSelectedView()).setTextColor(view.getResources().getColor(R.color.buttonsTextActive));
            //((TextView) view.spinnerLeft.getSelectedView()).setTextColor(view.getResources().getColor(R.color.buttonsText));
            ((TextView) view.spinnerLeft.getSelectedView()).setTextColor(Color.CYAN);
            ((TextView) view.spinnerRight.getSelectedView()).setTextColor(Color.YELLOW);
        }


    }
}






