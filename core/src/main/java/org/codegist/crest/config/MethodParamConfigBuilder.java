package org.codegist.crest.config;

import org.codegist.common.lang.State;
import org.codegist.common.lang.Validate;
import org.codegist.common.reflect.Methods;
import org.codegist.crest.CRestProperty;
import org.codegist.crest.HttpRequest;
import org.codegist.crest.serializer.Serializer;
import org.codegist.crest.serializer.SerializerRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;


@SuppressWarnings("unchecked")
public class MethodParamConfigBuilder extends ParamConfigBuilder<MethodParamConfig> {

    private final Class<?> clazz;
    private final Type genericType;
    private final SerializerRegistry serializerRegistry;
    private final Map<Class<? extends Annotation>, Annotation> paramAnnotation;

    private Serializer serializer;
    private boolean encoded;

    MethodParamConfigBuilder(MethodConfigBuilder parent, Class<?> clazz, Type genericType, Map<Class<? extends Annotation>, Annotation> paramAnnotation, Map<String, Object> customProperties) {
        super(parent, customProperties);
        this.clazz = clazz;
        this.genericType = genericType;
        this.paramAnnotation = paramAnnotation;
        this.serializerRegistry = (SerializerRegistry) customProperties.get(SerializerRegistry.class.getName());
    }

    /**
     * @inheritDoc
     */
    public MethodParamConfig build(boolean validateConfig, boolean isTemplate) {
        // make local copies so that we don't mess with builder state to be able to call build multiple times on it
        Serializer serializer = this.serializer;
        boolean encoded = this.encoded;

        if (!isTemplate) {
            serializer = defaultIfUndefined(serializer, CRestProperty.CONFIG_PARAM_DEFAULT_SERIALIZER, newInstance(MethodParamConfig.DEFAULT_SERIALIZER));
            encoded = defaultIfUndefined(encoded, CRestProperty.CONFIG_PARAM_DEFAULT_ENCODED, MethodParamConfig.DEFAULT_ENCODED);

            if (serializer == null) {
                State.notNull(serializerRegistry, "Can't lookup a serializer by type. Please provide a SerializerFactory");
                // if null, then choose which serializer to apply using default rules
                serializer = serializerRegistry.getForType(genericType);
            }
        }

        return new DefaultMethodParamConfig(
                super.build(validateConfig, isTemplate),
                serializer,
                encoded,
                paramAnnotation
        );
    }


    @Override
    public MethodParamConfigBuilder setIgnoreNullOrEmptyValues(boolean ignoreNullOrEmptyValues) {
        super.setIgnoreNullOrEmptyValues(ignoreNullOrEmptyValues);
        return this;
    }

    /**
     * Sets the argument's serializer. If not set, the system automatically choose a serializer based on the argument type. See {@link org.codegist.crest.CRest} for the selection rules.
     *
     * @param serializerClassName the serializer classname to use for this argument
     * @return current builder
     */
    public MethodParamConfigBuilder setSerializer(String serializerClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (ignore(serializerClassName)) return this;
        return setSerializer((Class<? extends Serializer>) Class.forName(replacePlaceholders(serializerClassName)));
    }

    /**
     * Sets the argument's serializer. If not set, the system automatically choose a serializer based on the argument type. See {@link org.codegist.crest.CRest} for the selection rules.
     *
     * @param serializer the serializer to use for this argument
     * @return current builder
     */
    public MethodParamConfigBuilder setSerializer(Class<? extends Serializer> serializer) throws IllegalAccessException, InstantiationException {
        if (ignore(serializer)) return this;
        return setSerializer(newInstance(serializer));
    }

    /**
     * Sets the argument's serializer. If not set, the system automatically choose a serializer based on the argument type. See {@link org.codegist.crest.CRest} for the selection rules.
     *
     * @param serializer the serializer to use for this argument
     * @return current builder
     */
    public MethodParamConfigBuilder setSerializer(Serializer serializer) {
        if (ignore(serializer)) return this;
        this.serializer = serializer;
        return this;
    }

    @Override
    public MethodParamConfigBuilder setName(String name) {
        return (MethodParamConfigBuilder) super.setName(name);
    }

    @Override
    public MethodParamConfigBuilder setDefaultValue(String defaultValue) {
        return (MethodParamConfigBuilder) super.setDefaultValue(defaultValue);
    }

    public MethodParamConfigBuilder forPath() {
        return setDestination(HttpRequest.DEST_PATH);
    }

    public MethodParamConfigBuilder forQuery() {
        return setDestination(HttpRequest.DEST_QUERY);
    }

    public MethodParamConfigBuilder forForm() {
        return setDestination(HttpRequest.DEST_FORM);
    }

    public MethodParamConfigBuilder forHeader() {
        return setDestination(HttpRequest.DEST_HEADER);
    }

    @Override
    public MethodParamConfigBuilder setDestination(String dest) {
        return (MethodParamConfigBuilder) super.setDestination(dest);
    }

    public MethodParamConfigBuilder setEncoded(boolean encoded) {
        this.encoded = encoded;
        return this;
    }
}
