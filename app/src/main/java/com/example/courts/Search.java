package com.example.courts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;

import io.paperdb.Paper;

public class Search extends AppCompatActivity {

    String[] courts  = {"ААА", "Басманный районный суд","Замоскворецкий районный суд", "Московский городской суд",
            "Мещанский районный суд","Пресненский районный суд","Таганский районный суд",
            "Тверской районный суд","Хамовнический районный", "Головинский районный суд", "Коптевский районный суд","Савёловский районный суд",
            "Тимирязевский районный суд", "Бабушкинский районный суд", "Бутырский районный суд", "Останкинский районный суд",
            "Измайловский районный суд", "Перовский районный суд", "Преображенский районный суд", "Кузьминский районный суд", "Лефортовский районный суд",
            "Люблинский районный суд", "Нагатинский районный суд", "Симоновский районный суд", "Чертановский районный суд", "Гагаринский районный суд", "Зюзинский районный суд",
            "Черёмушкинский районный суд", "Дорогомиловский районный суд", "Кунцевский районный суд", "Никулинский районный суд", "Солнцевский районный суд",
            "Тушинский районный суд", "Хорошёвский районный суд", "Зеленоградский районный суд"};
    String selected_court;

    String[] instance =  {"","Первая", "Апелляционная", "Кассационная", "Надзорная"};
    String selected_instance;

    String[] disturbance = {"Все типы судопроизводств", "Административное", "Гражданское",
            "Об административных правонарушениях", "Первичные документы", "Производство по материалам",
            "Уголовное"};
    String selected_disturbance;

    String unique_id;
    String number_case;
    String number_doc;
    String participants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Paper.init(this);

        // Делаем сверху строчку с названием приложения
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        // спиннер судов
        Spinner spinner_court = findViewById(R.id.spinner_courts);
        Arrays.sort(courts);
        courts[0] = "Все суды";

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, courts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_court.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                selected_court = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };
        spinner_court.setOnItemSelectedListener(itemSelectedListener);
        spinner_court.setAdapter(adapter);

        // спиннер инстанций
        Spinner spinner_instance = findViewById(R.id.spinner_instances);
        ArrayAdapter<String> adapter_instance = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, instance);
        adapter_instance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_instance.setAdapter(adapter_instance);
        AdapterView.OnItemSelectedListener itemSelectedListener_instance = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                selected_instance = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };
        spinner_instance.setOnItemSelectedListener(itemSelectedListener_instance);
        spinner_instance.setAdapter(adapter_instance);

        // спиннер произдоств
        Spinner spinner_disturbance = findViewById(R.id.spinner_disturbance);
        ArrayAdapter<String> adapter_disturbance = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, disturbance);
        adapter_disturbance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_disturbance.setAdapter(adapter_disturbance);
        AdapterView.OnItemSelectedListener itemSelectedListener_disturbance = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                selected_disturbance = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };
        spinner_disturbance.setOnItemSelectedListener(itemSelectedListener_disturbance);
        spinner_disturbance.setAdapter(adapter_disturbance);

        Button b = findViewById(R.id.button_search);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout textInputLayout_participants = findViewById(R.id.textInput_parts);
                participants = textInputLayout_participants.getEditText().getText().toString().trim();

                TextInputLayout textInputLayout_number_case = findViewById(R.id.textInput_number_case);
                number_case = textInputLayout_number_case.getEditText().getText().toString().trim();

                TextInputLayout textInputLayout_unique_id = findViewById(R.id.textInput_unique_id);
                unique_id = textInputLayout_unique_id.getEditText().getText().toString().trim();

                TextInputLayout textInputLayout_number_doc = findViewById(R.id.textInput_number_doc);
                number_doc = textInputLayout_number_doc.getEditText().getText().toString().trim();

                String link = new UrlGenerator(Paper.book().read("city"), selected_court,unique_id,
                        selected_instance, number_doc, number_case, participants, selected_disturbance).get_link();

                Toast.makeText(getApplicationContext(), "Загрузка...", Toast.LENGTH_SHORT).show();
                Log.d("Link", String.valueOf(number_case.length()));
                Intent intent = new Intent(Search.this, SearchResult.class);
                intent.putExtra("page", 1);
                intent.putExtra("link", link);
                startActivity(intent);
                Log.d("Link", link);

            }
        });



    }

}