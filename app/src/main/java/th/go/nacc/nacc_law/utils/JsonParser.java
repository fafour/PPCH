package th.go.nacc.nacc_law.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import th.go.nacc.nacc_law.model.News;
import th.go.nacc.nacc_law.model.SectionContent;
import th.go.nacc.nacc_law.model.Type;
import th.go.nacc.nacc_law.model.TypeSub;

/**
 * Created by error on 4/28/2017 AD.
 */

public class JsonParser {

    public static ArrayList<SectionContent> parseEducation(String result) {
        ArrayList<SectionContent> list = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(result);
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SectionContent sectionContent = new SectionContent();
                sectionContent.setAudio(jsonObject.getString("audio"));
                sectionContent.setClip(jsonObject.getString("clip"));
                sectionContent.setDetail(jsonObject.getString("detail"));
                sectionContent.setId(jsonObject.getInt("id"));
                sectionContent.setImage(jsonObject.getString("audio"));
                try {
                    sectionContent.setSection(jsonObject.getInt("section"));
                } catch (Exception e) {

                }
                sectionContent.setSeq(jsonObject.getInt("seq"));
                sectionContent.setTitle(jsonObject.getString("title"));
                list.add(sectionContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ArrayList<News> parseNews(String result) {
        ArrayList<News> list = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(result);
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                News news = new News();
                news.setId(jsonObject.getInt("id"));
                news.setTitle(jsonObject.getString("title"));
                news.setDetail(jsonObject.getString("detail"));
                news.setLink(jsonObject.getString("link"));
                news.setStartDate(jsonObject.getString("start_date"));
                news.setEndDate(jsonObject.getString("end_date"));
                news.setImage(jsonObject.getString("image"));
                news.setAudio(jsonObject.getString("audio"));
                news.setClip(jsonObject.getString("clip"));
                list.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ArrayList<Type> parseType(String result) {
        ArrayList<Type> list = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(result);
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Type news = new Type();
                news.id = i + "";
                news.Name = jsonObject.getString("name");

                try {
                    JSONArray jsonArray1 = jsonObject.getJSONArray("data");

                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                        TypeSub typeSub = new TypeSub();
                        typeSub.id = jsonObject1.getString("id");
                        typeSub.Name = jsonObject1.getString("name");
                        typeSub.org_id = jsonObject1.getString("org_id");
                        typeSub.idOrigin = jsonObject1.getString("id");
                        news.SubType.add(typeSub);
                    }
                }catch (Exception e){

                }

                list.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ArrayList<Type> parseMinistry(String result) {
        ArrayList<Type> list = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(result);
            JSONArray jsonArray = json.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Type news = new Type();
                news.id = jsonObject.getString("id");
                news.Name = jsonObject.getString("name");

                list.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
