package org.codegist.crest.config;


public interface PathBuilder {

    PathBuilder merge(String templateName, String templateValue, boolean encoded);

    String build();

}
