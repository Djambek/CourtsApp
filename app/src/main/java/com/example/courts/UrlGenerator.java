package com.example.courts;

import android.annotation.SuppressLint;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UrlGenerator {private String url;
    @SuppressLint("NewApi")
    private Map<String, String> msk_courts = Stream.of(new String[][] {
            { "Все суды", ""},
            {"mgs", "Московский городской суд"},
            {"babushkinskij", "Бабушкинский районный суд"},
            {"basmannyj", "Басманный районный суд"},
            {"butyrskij", "Бутырский районный суд"},
            {"gagarinskij", "Гагаринский районный суд"},
            {"golovinskij", "Головинский районный суд"},
            {"dorogomilovskij", "Дорогомиловский районный суд"},
            {"zamoskvoreckij", "Замоскворецкий районный суд"},
            {"zelenogradskij", "Зеленоградский районный суд"},
            {"zyuzinskij", "Зюзинский районный суд"},
            {"izmajlovskij", "Измайловский районный суд"},
            {"koptevskij", "Коптевский районный суд"},
            {"kuzminskij", "Кузьминский районный суд"},
            {"kuncevskij", "Кунцевский районный суд"},
            {"lefortovskij", "Лефортовский районный суд"},
            {"lyublinskij", "Люблинский районный суд"},
            {"meshchanskij", "Мещанский районный суд"},
            {"nagatinskij", "Нагатинский районный суд"},
            {"nikulinskij", "Никулинский районный суд"},
            {"ostankinskij", "Останкинский районный суд"},
            {"perovskij", "Перовский районный суд"},
            {"preobrazhenskij", "Преображенский районный суд"},
            {"presnenskij", "Пресненский районный суд"},
            {"savyolovskij", "Савёловский районный суд"},
            {"simonovskij", "Симоновский районный суд"},
            {"solncevskij", "Солнцевский районный суд"},
            {"taganskij", "Таганский районный суд"},
            {"tverskoj", "Тверской районный суд"},
            {"timiryazevskij", "Тимирязевский районный суд"},
            {"troickij", "Троицкий районный суд"},
            {"tushinskij", "Тушинский районный суд"},
            {"hamovnicheskij", "Хамовнический районный суд"},
            {"horoshevskij", "Хорошёвский районный суд"},
            {"cheryomushkinskij", "Черёмушкинский районный суд"},
            {"chertanovskij", "Чертановский районный суд"},
            {"shcherbinskij", "Щербинский районный суд"},
    }).collect(Collectors.collectingAndThen(
            Collectors.toMap(data -> data[1], data -> data[0]),
            Collections::<String, String> unmodifiableMap));

    @SuppressLint("NewApi")
    private Map<String, String> msk_instance = Stream.of(new String[][] {
            {" ", ""},
            {"Первая", "1"},
            {"Апелляционная", "2"},
            {"Кассационная", "3"},
            {"Надзорная", "4"},
    }).collect(Collectors.collectingAndThen(
            Collectors.toMap(data -> data[0], data -> data[1]),
            Collections::<String, String> unmodifiableMap));

    @SuppressLint("NewApi")
    private Map<String, String> msk_type_case = Stream.of(new String[][] {
            {"Все типы судопроизводств", ""},
            {"Административное", "1"},
            {"Гражданское", "2"},
            {"Об административных правонарушениях", "3"},
            {"Первичные документы", "4"},
            {"Производства по материалам", "5"},
            {"Уголовное", "6"},
    }).collect(Collectors.collectingAndThen(
            Collectors.toMap(data -> data[0], data -> data[1]),
            Collections::<String, String> unmodifiableMap));
    String type_case;
    String participant;
    String number_input_document;
    String unique_id;
    String type_trial;
    String number_case;
    String type_instance;

    UrlGenerator(String city, String type_trial, String unique_id, String type_instance,
                          String number_input_document, String number_case,
                          String participant, String type_case){

        this.unique_id = unique_id.length() > 0 ? unique_id:""; // Уникальный идентификатор дела
        this.number_input_document = number_input_document.length() > 0 ? number_input_document:""; // номер входящего документа
        this.number_case = number_case.length() > 0 ?  number_case:""; // номер дела
        this.participant = participant.length() > 0 ? participant:""; // стороны

        if (city.equals("Москва")){
            this.type_trial = msk_courts.get(type_trial) == null ? "":msk_courts.get(type_trial); // суд
            this.type_case = msk_type_case.get(type_case) == null ? "":msk_type_case.get(type_case); // производство
            this.type_instance = msk_instance.get(type_instance) == null ? "":msk_instance.get(type_instance); // Инстанция
            this.url = "https://mos-gorsud.ru/rs/golovinskij/search?formType=shortForm&courtAlias="+this.type_trial+
                    "&uid="+this.unique_id+"&instance="+this.type_instance+"&processType="+this.type_case+"&letterNumber="+
                    this.number_input_document+"&caseNumber="+this.number_case+"&participant="+this.participant;
        }



    }

    public String get_link(){
        return url;
    }

}
