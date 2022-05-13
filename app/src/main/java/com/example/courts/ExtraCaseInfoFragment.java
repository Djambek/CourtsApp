package com.example.courts;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ExtraCaseInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_extra_case_info, container, false);
        ArrayList<String> history = getArguments().getStringArrayList("history");
        LinearLayout history_layout  = view.findViewById(R.id.linlay_history);



        for (int i = 0; i < history.size()/3; i++) {
            View view_line = new View(getContext());
            view_line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            view_line.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            Spanned text = Html.fromHtml("<b>Дата: </b>"+history.get(i*3)+"<br><b>Статус: </b>"+history.get(i*3+1)+"<br><b>Документ: </b>"+ history.get(i*3+2));
            TextView textview_tmp = new TextView(getContext());
            textview_tmp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textview_tmp.setText(text);
            history_layout.addView(textview_tmp);
            if (i != history.size()/3-1){history_layout.addView(view_line);}
        }

        LinearLayout history_place_layout = view.findViewById(R.id.linlay_history_places);
        ArrayList<String> history_place = getArguments().getStringArrayList("history_place");
        for (int i = 0; i < history_place.size()/3; i++) {
            View view_line = new View(getContext());
            view_line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            view_line.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
            Spanned text = Html.fromHtml("<b>Дата: </b>"+history.get(i*3)+"<br><b>Местонахождение: </b>"+history.get(i*3+1)+"<br><b>Комментарий: </b>"+ history.get(i*3+2));
            TextView textview_tmp = new TextView(getContext());
            textview_tmp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textview_tmp.setText(text);
            history_place_layout.addView(textview_tmp);
            if (i != history_place.size()/3-1){history_place_layout.addView(view_line);}
            }


        TextView history_text = view.findViewById(R.id.text_history_status);
        history_text.setText(Html.fromHtml("<font><b>История состояний</b></font>"));

        TextView history_place_text = view.findViewById(R.id.text_history_place);
        history_place_text.setText(Html.fromHtml("<font><b>История местонахождения</b></font>"));

        TextView documents_text = view.findViewById(R.id.document_text);
        documents_text.setText(Html.fromHtml("<font><b>Судебные акты</b></font>"));

        TextView sessions_text = view.findViewById(R.id.text_sessions);
        sessions_text.setText(Html.fromHtml("<font><b>Судебные заседания и беседы</b></font>"));

        
        ArrayList<String> documents = getArguments().getStringArrayList("documents");
        LinearLayout documents_layout = view.findViewById(R.id.linlay_documents);
        for (int i = 0; i < documents.size()/3; i++) {
            Spanned text;
            TextView textview_tmp = new TextView(getContext());
            textview_tmp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if (!documents.get(i*3+2).equals("")){
                text = Html.fromHtml("<b>Дата документа: </b>"+documents.get(i*3)+"<br>"
                                    +"<b>Вид документа: </b>"+documents.get(i*3+1)+"<br>"
                                    +"<b>Документ: </b>"+"<a href=\""+documents.get(i*3+2)+"\"> скачать </a>");
                String link = documents.get(i*3+2);
                textview_tmp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(browserIntent);
                    }
                });
            }else{
                text = Html.fromHtml("<b>Дата документа: </b>"+documents.get(i*3)+"<br>"
                        +"<b>Вид документа: </b>"+documents.get(i*3+1)+"<br>"
                        +"<b>Документ: </b>");
            }
            View view_line = new View(getContext());
            view_line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            view_line.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));


            textview_tmp.setText(text);

            documents_layout.addView(textview_tmp);
            if (i != documents.size()/3-1){documents_layout.addView(view_line);}


        }



        ArrayList<String> sessions = getArguments().getStringArrayList("sessions");
        LinearLayout sessions_layout = view.findViewById(R.id.linlay_sessions);
        for (int i = 0; i < sessions.size()/6; i++) {
            Spanned text = Html.fromHtml("<b>Дата и время: </b>"+sessions.get(i*6)+"<br>"
                    +"<b>Зал: </b>"+sessions.get(i*6+1)+"<br>"+"<b>Стадия: </b>"+sessions.get(i*6+2)
                    +"<br>"+"<b>Результат: </b>"+sessions.get(i*6+3)+"<br>"+"<b>Основание: </b>"
                    +sessions.get(i*6+4)+"<br>"+"<b>Видеозапись: </b>"+sessions.get(i*6+5));

            View view_line = new View(getContext());
            view_line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
            view_line.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));

            TextView textview_tmp = new TextView(getContext());
            textview_tmp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            textview_tmp.setText(text);
            sessions_layout.addView(textview_tmp);
            if (i != sessions.size()/6-1){sessions_layout.addView(view_line);}
        }

        return view;
    }
}