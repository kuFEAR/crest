package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.lang.Validate;
import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.Registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ParamConfigBuilder<T extends ParamConfig> extends ConfigBuilder<T> {

    private final MethodConfigBuilder parent;

    private final Map<String,Object> metas = new Hashtable<String, Object>();

    private final Class<?> clazz;
    private final Type genericType;
    private final Registry<Class<?>, Serializer> classSerializerRegistry;
    private final Map<Class<? extends Annotation>, Annotation> paramAnnotation;

    private String name;
    private String defaultValue;
    private String dest;
    private String listSeparator;
    private Serializer serializer;
    private Boolean encoded;

    ParamConfigBuilder(MethodConfigBuilder parent, Map<String, Object> customProperties) {
        this(parent, customProperties, String.class, String.class, Collections.<Class<? extends Annotation>, Annotation>emptyMap());
    }
    ParamConfigBuilder(MethodConfigBuilder parent, Map<String, Object> customProperties, Class<?> clazz, Type genericType, Map<Class<? extends Annotation>, Annotation> paramAnnotation) {
        super(customProperties);
        this.parent = parent;
        this.clazz = Types.getComponentClass(clazz, genericType);
        this.genericType = Types.getComponentType(clazz, genericType);
        this.paramAnnotation = paramAnnotation;
        this.classSerializerRegistry = (Registry<Class<?>, Serializer>) customProperties.get(Registry.class.getName() + "#serializers-per-class");
    }

    /**
     * @inheritDoc
     */
    public T build(boolean validateConfig, boolean isTemplate) {
        // make local copies so that we don't mess with builder state to be able to call build multiple times on it
        String name = this.name;
        String defaultValue = this.defaultValue;
        String dest = this.dest;
        String listSeparator = this.listSeparator;
        Serializer serializer = this.serializer;
        Boolean encoded = this.encoded;
        Map<String,Object> metas = this.metas;
        Class<?> clazz = this.clazz;
        Type genericType = this.genericType;

        if (!isTemplate) {
            name = defaultIfUndefined(name, CRestProperty.CONFIG_PARAM_DEFAULT_NAME, ParamConfig.DEFAULT_NAME);
            defaultValue = defaultIfUndefined(defaultValue, CRestProperty.CONFIG_PARAM_DEFAULT_VALUE, ParamConfig.DEFAULT_VALUE);
            dest = defaultIfUndefined(dest, CRestProperty.CONFIG_PARAM_DEFAULT_DESTINATION, ParamConfig.DEFAULT_DESTINATION);
            metas = defaultIfUndefined(metas, CRestProperty.CONFIG_PARAM_DEFAULT_METAS, ParamConfig.DEFAULT_METADATAS);
            listSeparator = defaultIfUndefined(listSeparator, CRestProperty.CONFIG_PARAM_DEFAULT_LIST_SEPARATOR, null);
            serializer = defaultIfUndefined(serializer, CRestProperty.CONFIG_PARAM_DEFAULT_SERIALIZER, newInstance(ParamConfig.DEFAULT_SERIALIZER));
            encoded = defaultIfUndefined(encoded, CRestProperty.CONFIG_PARAM_DEFAULT_ENCODED, (HttpRequest.DEST_COOKIE.equals(dest) || HttpRequest.DEST_HEADER.equals(dest)) ? Boolean.TRUE : ParamConfig.DEFAULT_ENCODED);
            if (serializer == null) {
                State.notNull(classSerializerRegistry, "Can't lookup a serializer by type. Please provide a ClassSerializerRegistry");
                // if null, then choose which serializer to apply using default rules
                serializer = classSerializerRegistry.get(clazz);
            }
        }

        if (validateConfig) {
            Validate.notBlank(name, "Parameter must have a name");
        }

        return (T) new DefaultParamConfig(
                genericType,
                clazz,
                name,
                defaultValue,
                dest,
                listSeparator,
                metas,
                serializer,
                encoded,
                paramAnnotation);
    }

    public MethodConfigBuilder endParamConfig() {
        return parent;
    }

    public ParamConfigBuilder forQuery() {
        return setDestination(HttpRequest.DEST_QUERY);
    }
    public ParamConfigBuilder forPath() {
        return setDestination(HttpRequest.DEST_PATH);
    }
    public ParamConfigBuilder forMatrix() {
        return setDestination(HttpRequest.DEST_MATRIX);
    }
    public ParamConfigBuilder forCookie() {
        return setDestination(HttpRequest.DEST_COOKIE);
    }
    public ParamConfigBuilder forHeader() {
        return setDestination(HttpRequest.DEST_HEADER);
    }
    public ParamConfigBuilder forForm() {
        return setDestination(HttpRequest.DEST_FORM);
    }
    public ParamConfigBuilder forMultiPart() {
        return forForm();
    }

    @Override
    public ParamConfigBuilder setIgnoreNullOrEmptyValues(boolean ignoreNullOrEmptyValues) {
        super.setIgnoreNullOrEmptyValues(ignoreNullOrEmptyValues);
        return this;
    }

    public ParamConfigBuilder setName(String name) {
        if (ignore(name)) return this;
        this.name = replacePlaceholders(name);
        return this;
    }


    public ParamConfigBuilder setDefaultValue(String defaultValue) {
        if (ignore(defaultValue)) return this;
        this.defaultValue = replacePlaceholders(defaultValue);
        return this;
    }

    public ParamConfigBuilder setDestination(String dest) {
        if (ignore(dest)) return this;
        this.dest = replacePlaceholders(dest);
        return this;
    }

    public ParamConfigBuilder setListSeparator(String listSeparator) {
        if (ignore(listSeparator)) return this;
        this.listSeparator = listSeparator;
        return this;
    }

    public ParamConfigBuilder setEncoded(Boolean encoded) {
        this.encoded = encoded;
        return this;
    }

    public ParamConfigBuilder setMetaDatas(Map<String,Object> metadatas) {
        if (ignore(metadatas)) return this;
        this.metas.clear();
        this.metas.putAll(metadatas);
        return this;
    }

    /**
     * Sets the argument's serializer. If not set, the system automatically choose a serializer based on the argument type. See {@link org.codegist.crest.CRest} for the selection rules.
     *
     * @param serializer the serializer to use for this argument
     * @return current builder
     */
    public ParamConfigBuilder setSerializer(Class<? extends Serializer> serializer) {
        if (ignore(serializer)) return this;
        this.serializer = newInstance(serializer);
        return this;
    }
}