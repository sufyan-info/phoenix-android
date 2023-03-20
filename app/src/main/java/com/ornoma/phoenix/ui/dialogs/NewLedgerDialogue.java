package com.ornoma.phoenix.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

import com.ornoma.phoenix.R;
import com.ornoma.phoenix.core.Ledger;
import com.ornoma.phoenix.factory.MasterCache;

/**
 * Created by de76 on 5/5/17.
 */

@SuppressWarnings("FieldCanBeLocal")
public abstract class NewLedgerDialogue extends Dialog{
    private Context context;
    private MasterCache masterCache;
    private Random random;

    private EditText editTextName;
    private EditText editTextId;
    private Button buttonAdd;
    private Button buttonCancel;

    private String ledgerName;
    private int ledgerId;
    private int[] ledgerIdArray;

    public abstract void onSubmitNewLedger(Ledger ledger);

    public NewLedgerDialogue(@NonNull Context context) {
        super(context);
        masterCache = new MasterCache(context);
        random = new Random();
        ledgerIdArray = masterCache.getLedgerIds();
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private void nextRandomId(){
        int idToTest, ledgerId;
        boolean randomOk = false;
        boolean testIsGood;
        do {
            testIsGood = true;
            idToTest = gerRandomId();
            for (int i = 0; i < ledgerIdArray.length; i++){
                ledgerId = ledgerIdArray[i];
                if (idToTest == ledgerId)
                    testIsGood = false;
            }
            if (testIsGood)
                randomOk = true;
        }
        while (!randomOk);
        this.ledgerId = idToTest;
    }

    private int getRandomSingleDigit(){
        int rand, result;
        do{
            rand = Math.abs(random.nextInt());
            result = rand % 10;
        }
        while (result == 0);
        return result;
    }

    private int gerRandomId(){
        int rand = Math.abs(random.nextInt());
        int result = rand % 1000;
        if (result < 100){
            result += getRandomSingleDigit() * 100;
        }
        return result;
    }

    @Override
    public void show(){
        bindDialogue();
        bindLedgerId();
        bindActions();
        super.show();
    }

    private void bindActions(){
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                submitLedger();}});
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                NewLedgerDialogue.this.dismiss();}});
    }

    private void submitLedger(){
        if (!(ledgerName.length() > 0))
            return;
        Ledger ledger = new Ledger();
        ledger.setName(ledgerName);
        ledger.setId(ledgerId);
        dismiss();
        onSubmitNewLedger(ledger);
    }

    private void bindLedgerId(){
        nextRandomId();
        editTextId.setText(String.valueOf(ledgerId));
    }

    private void bindDialogue(){
        setContentView(R.layout.dialogue_new_ledger);
        editTextId = (EditText)findViewById(R.id.editText_id);
        editTextName = (EditText)findViewById(R.id.editText_name);
        buttonAdd = (Button)findViewById(R.id.button_add);
        buttonCancel = (Button)findViewById(R.id.button_cancel);

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                ledgerName = s.toString();}});
        editTextId.setEnabled(false);
    }

}
