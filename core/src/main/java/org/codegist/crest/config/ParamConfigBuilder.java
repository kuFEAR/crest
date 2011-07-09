package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.lang.Validate;
import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.io.http.HttpRequest;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.Registry;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ParamConfigBuilder<T extends ParamConfig> extends ConfigBuilder<T> {

    private final MethodConfigBuilder parent;

    private final Map<String,Object> metas = new Hashtable<String, Object>();

    private final Class<?> clazz;
    private final Type genericType;
    private final Registry<Class<?>, Serializer> classSerializerRegistry;

    private String name;
    private String defaultValue;
    private String dest;
    private String listSeparator;
    private Serializer serializer;
    private Boolean encoded;

    ParamConfigBuilder(MethodConfigBuilder parent, Map<String, Object> crestProperties) {
        this(parent, crestProperties, String.class, String.class);
    }
    ParamConfigBuilder(MethodConfigBuilder parent, Map<String, Object> crestProperties, Class<?> clazz, Type genericType) {
        super(crestProperties);
        this.parent = parent;
        this.clazz = Types.getComponentClass(clazz, genericType);
        this.genericType = Types.getComponentType(clazz, genericType);
        this.classSerializerRegistry = (Registry<Class<?>, Serializer>) crestProperties.get(Registry.class.getName() + "#serializers-per-class");
    }

    /**
     * @inheritDoc
     */
    public T build(boolean validateConfig, boolean isTemplate) {
        // make local copies so that we don't mess with builder state to be able to call build multiple times on it
        String pName = this.name;
        String pDefaultValue = this.defaultValue;
        String pDest = this.dest;
        String pListSeparator = this.listSeparator;
        Serializer pSerializer = this.serializer;
        Boolean pEncoded = this.encoded;
        Map<String,Object> pMetas = this.metas;

        if (!isTemplate) {
            pName = defaultIfUndefined(pName, CRestProperty.CONFIG_PARAM_DEFAULT_NAME, ParamConfig.DEFAULT_NAME);
            pDefaultValue = defaultIfUndefined(pDefaultValue, CRestProperty.CONFIG_PARAM_DEFAULT_VALUE, ParamConfig.DEFAULT_VALUE);
            pDest = defaultIfUndefined(pDest, CRestProperty.CONFIG_PARAM_DEFAULT_DESTINATION, ParamConfig.DEFAULT_DESTINATION);
            pMetas = defaultIfUndefined(pMetas, CRestProperty.CONFIG_PARAM_DEFAULT_METAS, ParamConfig.DEFAULT_METADATAS);
            pListSeparator = defaultIfUndefined(pListSeparator, CRestProperty.CONFIG_PARAM_DEFAULT_LIST_SEPARATOR, null);
            pSerializer = defaultIfUndefined(pSerializer, CRestProperty.CONFIG_PARAM_DEFAULT_SERIALIZER, newInstance(ParamConfig.DEFAULT_SERIALIZER));
            pEncoded = defaultIfUndefined(pEncoded, CRestProperty.CONFIG_PARAM_DEFAULT_ENCODED, (HttpRequest.DEST_COOKIE.equals(pDest) || HttpRequest.DEST_HEADER.equals(pDest)) ? Boolean.TRUE : ParamConfig.DEFAULT_ENCODED);
            if (pSerializer == null) {
                State.notNull(classSerializerRegistry, "Can't lookup a serializer by type. Please provide a ClassSerializerRegistry");
                // if null, then choose which serializer to apply using default rules
                pSerializer = classSerializerRegistry.get(clazz);
            }
        }

        if (validateConfig) {
            Validate.notBlank(pName, "Parameter must have a name");
        }

        return (T) new DefaultParamConfig(
                genericType,
                clazz,
                pName,
                pDefaultValue,
                pDest,
                pListSeparator,
                pMetas,
                pSerializer,
                pEncoded);
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

    public ParamConfigBuilder setName(String name) {
        this.name = replacePlaceholders(name);
        return this;
    }


    public ParamConfigBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = replacePlaceholders(defaultValue);
        return this;
    }

    public ParamConfigBuilder setDestination(String dest) {
        this.dest = replacePlaceholders(dest);
        return this;
    }

    public ParamConfigBuilder setListSeparator(String listSeparator) {
        this.listSeparator = listSeparator;
        return this;
    }

    public ParamConfigBuilder setEncoded(Boolean encoded) {
        this.encoded = encoded;
        return this;
    }

    public ParamConfigBuilder setMetaDatas(Map<String,Object> metadatas) {
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
        this.serializer = newInstance(serializer);
        return this;
    }
}