package com.example.electricbill;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText input_kwh;
    private SeekBar rebateBar;
    private TextView rebateText, output_Bill;
    private Button btn_Calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        input_kwh = findViewById(R.id.input_kwh);
        input_kwh.requestFocus();
        rebateBar = findViewById(R.id.rebateBar);
        rebateText = findViewById(R.id.rebateText);
        output_Bill = findViewById(R.id.output_bill);
        btn_Calculate = findViewById(R.id.btn_calculate);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        rebateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rebateText.setText("Rebate (" + progress + "%)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_Calculate.setOnClickListener(v -> {
            String kwhInput = input_kwh.getText().toString();

            if (kwhInput.isEmpty()) {
                Toast.makeText(this, "Please enter kWh consumption", Toast.LENGTH_SHORT).show();
                return;
            }

            double kWh = Double.parseDouble(kwhInput);
            int rebatePercentage = rebateBar.getProgress();
            double finalBill = calculateElectricBill(kWh, rebatePercentage);

            output_Bill.setText(String.format("Final Bill: RM %.2f", finalBill));
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menu_About) {
            Toast.makeText(this,"about clicked",Toast.LENGTH_SHORT).show();
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");
        builder.setMessage("Developer Info\n\n"
                + "Name: Muhammad Firdaus Zuhairi Bin Zailani\n"
                + "Matric No: 2022455736\n"
                + "Group: RCDCS2515A\n"
                + "Lecturer: Sir Mohammad Hafiz Bin Ismail\n"
                + "Copyright 2024\n"
                + "Visit: https://github.com/FirdausZuhairiIX?tab=repositories");

        // Add a clickable URL
        builder.setPositiveButton("Visit Website", (dialog, which) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/FirdausZuhairiIX?tab=repositories"));
            startActivity(browserIntent);
        });

        // Add a dismiss button
        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private double calculateElectricBill(double kWh, int rebatePercentage) {
        double cost = 0;

        if (kWh > 600) {
            cost += (kWh - 600) * 0.546;
            kWh = 600;
        }
        if (kWh > 300) {
            cost += (kWh - 300) * 0.516;
            kWh = 300;
        }
        if (kWh > 200) {
            cost += (kWh - 200) * 0.334;
            kWh = 200;
        }
        if (kWh > 0) {
            cost += kWh * 0.218;
        }

        double rebate = cost * rebatePercentage / 100.0;
        return cost - rebate;
    }
}
