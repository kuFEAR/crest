package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.lang.Validate;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.SerializerRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@SuppressWarnings("unchecked")
public class ParamConfigBuilder<T extends ParamConfig> extends AbstractConfigBuilder<T> {

    private final MethodConfigBuilder parent;

    private final Map<String,Object> metas = new Hashtable<String, Object>();

    private final Class<?> clazz;
    private final Type genericType;
    private final SerializerRegistry serializerRegistry;
    private final Map<Class<? extends Annotation>, Annotation> paramAnnotation;

    private String name;
    private String defaultValue;
    private String dest;
    private String listSeparator;
    private Serializer serializer;
    private boolean encoded;

    private static final Map<String,String> SEPARATOR = new HashMap<String, String>();
    static{
        SEPARATOR.put(HttpRequest.DEST_COOKIE, ";");
        SEPARATOR.put(HttpRequest.DEST_HEADER, ",");
    }

    ParamConfigBuilder(MethodConfigBuilder parent, Map<String, Object> customProperties) {
        this(parent, customProperties, String.class, String.class, Collections.<Class<? extends Annotation>, Annotation>emptyMap());
    }
    ParamConfigBuilder(MethodConfigBuilder parent, Map<String, Object> customProperties, Class<?> clazz, Type genericType, Map<Class<? extends Annotation>, Annotation> paramAnnotation) {
        super(customProperties);
        this.parent = parent;
        this.clazz = clazz;
        this.genericType = genericType;
        this.paramAnnotation = paramAnnotation;
        this.serializerRegistry = (SerializerRegistry) customProperties.get(SerializerRegistry.class.getName());
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
        boolean encoded = this.encoded;
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
            encoded = defaultIfUndefined(encoded, CRestProperty.CONFIG_PARAM_DEFAULT_ENCODED, ParamConfig.DEFAULT_ENCODED);
            if (serializer == null) {
                State.notNull(serializerRegistry, "Can't lookup a serializer by type. Please provide a SerializerFactory");
                // if null, then choose which serializer to apply using default rules
                serializer = serializerRegistry.getForType(genericType);
            }
        }


        if (validateConfig) {
            Validate.notBlank(name, "Parameter must have a name");
        }


        // todo is that good enough?
        if(clazz.isArray()) {
            clazz = clazz.getComponentType();
            genericType = clazz;
        }else if(Collection.class.isAssignableFrom(clazz)){
            genericType = ((ParameterizedType)genericType).getActualTypeArguments()[0];
            clazz = (Class<?>)genericType;
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

    public ParamConfigBuilder setEncoded(boolean encoded) {
        this.encoded = encoded;
        return this;
    }

    public ParamConfigBuilder setMetaDatas(Map<String,Object> metadatas) {
        if (ignore(metadatas)) return this;
        this.metas.clear();
        this.metas.putAll(metadatas);
        return this;
    }

    public ParamConfigBuilder addMetaDatas(String name, Object value) {
        if (ignore(name) && ignore(value)) return this;
        this.metas.clear();
        this.metas.put(name, value);
        return this;
    }


    /**
     * Sets the argument's serializer. If not set, the system automatically choose a serializer based on the argument type. See {@link org.codegist.crest.CRest} for the selection rules.
     *
     * @param serializerClassName the serializer classname to use for this argument
     * @return current builder
     */
    public ParamConfigBuilder setSerializer(String serializerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (ignore(serializerClassName)) return this;
        return setSerializer((Class<? extends Serializer>) Class.forName(replacePlaceholders(serializerClassName)));
    }

    /**
     * Sets the argument's serializer. If not set, the system automatically choose a serializer based on the argument type. See {@link org.codegist.crest.CRest} for the selection rules.
     *
     * @param serializer the serializer to use for this argument
     * @return current builder
     */
    public ParamConfigBuilder setSerializer(Class<? extends Serializer> serializer) throws IllegalAccessException, InstantiationException {
        if (ignore(serializer)) return this;
        return setSerializer(newInstance(serializer));
    }

    /**
     * Sets the argument's serializer. If not set, the system automatically choose a serializer based on the argument type. See {@link org.codegist.crest.CRest} for the selection rules.
     *
     * @param serializer the serializer to use for this argument
     * @return current builder
     */
    public ParamConfigBuilder setSerializer(Serializer serializer) {
        if (ignore(serializer)) return this;
        this.serializer = serializer;
        return this;
    }

}