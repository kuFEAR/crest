package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.lang.Validate;
import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.io.http.param.ParamProcessor;
import org.codegist.crest.io.http.param.ParamType;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.Registry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Map;

import static org.codegist.crest.CRestProperty.*;
import static org.codegist.crest.config.ParamConfig.*;
import static org.codegist.crest.io.http.param.ParamProcessors.select;
import static org.codegist.crest.io.http.param.ParamType.*;

@SuppressWarnings("unchecked")
public class ParamConfigBuilder<T extends ParamConfig> extends ConfigBuilder<T> {

    private final MethodConfigBuilder parent;

    private final Map<String,Object> metas = new Hashtable<String, Object>();

    private final Class<?> clazz;
    private final Type genericType;
    private final Registry<Class<?>, Serializer> classSerializerRegistry;

    private String name;
    private String defaultValue;
    private ParamType type;
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
        this.classSerializerRegistry = CRestProperty.get(crestProperties, Registry.class.getName() + "#serializers-per-class");
    }

    /**
     * @inheritDoc
     */
    public T build() throws Exception {
        String pName = defaultIfUndefined(this.name, PARAM_CONFIG_DEFAULT_NAME, DEFAULT_NAME);
        String pDefaultValue = defaultIfUndefined(this.defaultValue, PARAM_CONFIG_DEFAULT_VALUE, DEFAULT_VALUE);
        ParamType pType = defaultIfUndefined(this.type, PARAM_CONFIG_DEFAULT_TYPE, DEFAULT_TYPE);
        Map<String,Object> pMetas = defaultIfUndefined(this.metas, PARAM_CONFIG_DEFAULT_METAS, DEFAULT_METADATAS);
        String pListSeparator = defaultIfUndefined(this.listSeparator, PARAM_CONFIG_DEFAULT_LIST_SEPARATOR, null);
        Serializer pSerializer = defaultIfUndefined(this.serializer, PARAM_CONFIG_DEFAULT_SERIALIZER, DEFAULT_SERIALIZER);
        Boolean pEncoded = defaultIfUndefined(this.encoded, PARAM_CONFIG_DEFAULT_ENCODED, (COOKIE.equals(pType) || HEADER.equals(pType)) ? Boolean.TRUE : DEFAULT_ENCODED);
        if (pSerializer == null) {
            State.notNull(classSerializerRegistry, "Can't lookup a serializer by type. Please provide a ClassSerializerRegistry");
            // if null, then choose which serializer to apply using default rules
            pSerializer = classSerializerRegistry.get(clazz);
        }
        ParamProcessor paramProcessor =  defaultIfUndefined(null, PARAM_CONFIG_DEFAULT_PROCESSOR, newInstance(DEFAULT_PARAM_PROCESSOR));
        if(paramProcessor == null) {
            paramProcessor = select(pType, pListSeparator);
        }

        Validate.notBlank(pName, "Parameter must have a name");
        return (T) new DefaultParamConfig(
                genericType,
                clazz,
                pName,
                pDefaultValue,
                pType,
                pMetas,
                pSerializer,
                pEncoded,
                paramProcessor);
    }

    public MethodConfigBuilder endParamConfig() {
        return parent;
    }

    public ParamConfigBuilder forQuery() {
        return setType(QUERY);
    }
    public ParamConfigBuilder forPath() {
        return setType(PATH);
    }
    public ParamConfigBuilder forMatrix() {
        return setType(MATRIX);
    }
    public ParamConfigBuilder forCookie() {
        return setType(COOKIE);
    }
    public ParamConfigBuilder forHeader() {
        return setType(HEADER);
    }
    public ParamConfigBuilder forForm() {
        return setType(FORM);
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

    public ParamConfigBuilder setType(ParamType type) {
        this.type = type;
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
    public ParamConfigBuilder setSerializer(Class<? extends Serializer> serializer) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        this.serializer = newInstance(serializer);
        return this;
    }
}