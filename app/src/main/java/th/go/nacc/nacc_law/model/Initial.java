package th.go.nacc.nacc_law.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import th.go.nacc.nacc_law.AbstractActivity;
import th.go.nacc.nacc_law.MainActivity;
import th.go.nacc.nacc_law.R;
import th.go.nacc.nacc_law.application.MyApplication;

/**
 * Created by nontachai on 7/10/15 AD.
 */
public class Initial {

    private List<Section> sections;
    private List<SectionContent> content100;
    private List<SectionContent> content103;
    private List<SectionPDF> pdf100;
    private List<SectionPDF> pdf103;
    private List<Terminology> terms;
    private List<TerminologyPDF> termsPDF;

    public static Initial convertToObject ( JSONObject data ) {
        if ( data == null ) return null;

        Initial initial = new Initial();

        initial.sections = Section.convertToArray(data.optJSONArray("sections"));
        initial.terms = Terminology.convertToArray( data.optJSONArray("term"));
        initial.termsPDF = TerminologyPDF.convertToArray(data.optJSONArray("termPDF"));

        if ( data.isNull( "content" ) ) {
            initial.content100 = new ArrayList<SectionContent>();
            initial.content103 = new ArrayList<SectionContent>();
        } else {
            JSONObject content = data.optJSONObject("content");

            initial.content100 = SectionContent.convertToArray( content.optJSONArray( "section_100"));
            initial.content103 = SectionContent.convertToArray( content.optJSONArray( "section_103"));
        }

        if ( data.isNull( "pdf" ) ) {
            initial.pdf100 = new ArrayList<SectionPDF>();
            initial.pdf103 = new ArrayList<SectionPDF>();
        } else {
            JSONObject pdf = data.optJSONObject("pdf");

            initial.pdf100 = SectionPDF.convertToArray( pdf.optJSONArray("section_100"));
            initial.pdf103 = SectionPDF.convertToArray( pdf.optJSONArray("section_103"));
        }

        return initial;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<SectionContent> getContent100() {
        return content100;
    }

    public void setContent100(List<SectionContent> content100) {
        this.content100 = content100;
    }

    public List<SectionContent> getContent103() {
        return content103;
    }

    public void setContent103(List<SectionContent> content103) {
        this.content103 = content103;
    }

    public List<SectionPDF> getPdf100() {
        return pdf100;
    }

    public void setPdf100(List<SectionPDF> pdf100) {
        this.pdf100 = pdf100;
    }

    public List<SectionPDF> getPdf103() {
        return pdf103;
    }

    public void setPdf103(List<SectionPDF> pdf103) {
        this.pdf103 = pdf103;
    }

    public List<Terminology> getTerms() {
        return terms;
    }

    public void setTerms(List<Terminology> terms) {
        this.terms = terms;
    }

    public List<TerminologyPDF> getTermsPDF() {
        return termsPDF;
    }

    public void setTermsPDF(List<TerminologyPDF> termsPDF) {
        this.termsPDF = termsPDF;
    }


    public static void get ( final Context context, final boolean isShowProgress, final  OnResponseListener listerner ) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();

        String url = String.format(
                "%s/getInitial/%s",
                MyApplication.getInstance().API_HOSTNAME,
                MyApplication.getInstance().API_KEY
        );

        Log.i( "INITIAL_TAG", "URL: " + url );

        if ( isShowProgress ) {
            // show progress
            ((MainActivity)context).showProgress ( true );
        }

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {

                        if ( isShowProgress ) {
                            // hide progress
                            ((MainActivity)context).showProgress ( false );
                        }


                        // Is this response equal to old response (if any)?
                        boolean isUpdated = true;


                        // save preference
                        SharedPreferences sharedpreferences = context.getSharedPreferences(MyApplication.TAG, Context.MODE_PRIVATE);

                        String initialToken = sharedpreferences.getString("initial", null);

                        if ( initialToken == null ) isUpdated = true;
                        else {
                            if ( initialToken.equals( s ) ) {
                                isUpdated = false;
                            } else {
                                isUpdated = true;
                            }
                        }

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString( "initial", s );

                        editor.commit();

                        try {
                            JSONObject responseObject = new JSONObject( s );

                            if ( responseObject.optInt ( "status" ) == 0 ) {
                                JSONObject error = responseObject.optJSONObject( "error" );

                                Error serverError = new Error( error.optString( "detail" ) );

                                listerner.onGetListener( false, null, serverError );

                                return;
                            } else {

                                JSONObject data = responseObject.optJSONObject( "data" );

                                Initial initial = Initial.convertToObject( data );

                                listerner.onGetListener( isUpdated, initial, null );

                                return;
                            }
                        } catch ( JSONException e ) {
                            Error parseError = new Error(
                                    context.getResources().getString(R.string.error_json_parse)
                            );

                            listerner.onGetListener( false, null, parseError );

                            return;
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if ( isShowProgress ) {
                            // hide progress
                            ((AbstractActivity)context).showProgress ( false );
                        }

                        Error networkError = null;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            networkError = new Error( context.getString( R.string.error_network_timeout ));
                        } else if (error instanceof AuthFailureError) {
                            networkError = new Error( context.getString( R.string.error_auth_failure ));
                        } else if (error instanceof ServerError) {
                            networkError = new Error( context.getString( R.string.error_server ));
                        } else if (error instanceof NetworkError) {
                            networkError = new Error( context.getString( R.string.error_network ));
                        } else if (error instanceof ParseError) {
                            networkError = new Error( context.getString( R.string.error_json_parse ));
                        }

                        if ( networkError != null ) {
                            listerner.onGetListener(false, null, networkError);
                        }

                        return;
                    }
                }
        );

        MyApplication.getInstance().addToRequestQueue( request );

    }

    public interface OnResponseListener {
        public void onGetListener ( boolean isUpdated, Initial initial, Error error);
    }
}
