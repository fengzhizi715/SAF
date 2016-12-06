package com.test.saf.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tony Shen on 2016/12/1.
 */

public class MMPicsResponse extends HttpResponse {

    public List<Pic> tngou;

    public static class Pic implements Serializable {

        private static final long serialVersionUID = 3376457660406783270L;

        public String img;
        public String title;

    }
}
