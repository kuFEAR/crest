package org.codegist.crest.config;

import org.codegist.common.reflect.Types;
import org.codegist.crest.CRestConfig;
import org.codegist.crest.param.ParamProcessor;
import org.codegist.crest.param.ParamProcessorFactory;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.util.Registry;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.codegist.crest.config.ParamConfig.*;
import static org.codegist.crest.config.ParamType.COOKIE;
import static org.codegist.crest.config.ParamType.HEADER;

@SuppressWarnings("unchecked")
class DefaultParamConfigBuilder extends ConfigBuilder implements ParamConfigBuilder {

    private final Class<?> clazz;
    private final Type genericType;
    private final Registry<Class<?>, Serializer> classSerializerRegistry;
    private final Class<? extends ParamProcessor> paramProcessor;
    private final Map<String,Object> metas = new HashMap<String,Object>();

    private String name = null;
    private String defaultValue = null;
    private ParamType type = ParamType.getDefault();
    private String listSeparator = null;
    private Class<? extends Serializer> serializer = null;
    private Boolean encoded = false;

    DefaultParamConfigBuilder(CRestConfig crestConfig, Registry<Class<?>, Serializer> classSerializerRegistry, Class<?> clazz, Type genericType) {
        super(crestConfig);
        this.clazz = Types.getComponentClass(clazz, genericType);
        this.genericType = Types.getComponentType(clazz, genericType);
        this.classSerializerRegistry = classSerializerRegistry;

        this.name = override(PARAM_CONFIG_DEFAULT_NAME, this.name);
        this.defaultValue = override(PARAM_CONFIG_DEFAULT_VALUE, this.defaultValue);
        this.type = override(PARAM_CONFIG_DEFAULT_TYPE, this.type);
        this.listSeparator = override(PARAM_CONFIG_DEFAULT_LIST_SEPARATOR, this.listSeparator);
        this.serializer = override(PARAM_CONFIG_DEFAULT_SERIALIZER, this.serializer);
        this.encoded = override(PARAM_CONFIG_DEFAULT_ENCODED, this.encoded);
        this.paramProcessor = override(PARAM_CONFIG_DEFAULT_PROCESSOR, null);

        Map<String,Object> pMetas = override(PARAM_CONFIG_DEFAULT_METAS, this.metas);
        if(pMetas != this.metas) {
            this.metas.clear();
            this.metas.putAll(pMetas);
        }
    }

    /**
     * @inheritDoc
     */
    public ParamConfig build() throws Exception {
        return new DefaultParamConfig(
                genericType,
                clazz,
                name,
                defaultValue,
                type,
                metas,
                serializer != null ?  instantiate(serializer) : classSerializerRegistry.get(clazz),
                (COOKIE.equals(type) || HEADER.equals(type)) ? true : encoded,
                paramProcessor != null ? instantiate(paramProcessor) : ParamProcessorFactory.newInstance(type, listSeparator));
    }

    public ParamConfigBuilder setName(String name) {
        this.name = name;
        return this;
    }


    public ParamConfigBuilder setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public ParamConfigBuilder setEncoded(boolean encoded) {
        this.encoded = encoded;
        return this;
    }

    public ParamConfigBuilder setMetaDatas(Map<String,Object> metadatas) {
        this.metas.clear();
        this.metas.putAll(metadatas);
        return this;
    }

    public ParamConfigBuilder setSerializer(Class<? extends Serializer> serializer) {
        this.serializer = serializer;
        return this;
    }
}