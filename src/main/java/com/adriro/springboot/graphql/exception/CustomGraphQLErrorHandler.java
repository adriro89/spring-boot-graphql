package com.adriro.springboot.graphql.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CustomGraphQLErrorHandler implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable throwable, DataFetchingEnvironment environment) {
        GraphQLError error;

        if (throwable instanceof AuthorNotFoundException) {
            error = GraphqlErrorBuilder.newError()
                    .message(throwable.getMessage())
                    .errorType(ErrorType.NOT_FOUND)
                    .build();
        } else if (throwable instanceof BookNotFoundException) {
            error = GraphqlErrorBuilder.newError()
                    .message(throwable.getMessage())
                    .errorType(ErrorType.NOT_FOUND)
                    .build();
        } else {
            error = GraphqlErrorBuilder.newError()
                    .message("Unexpected error occurred: " + throwable.getMessage())
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .build();
        }

        return Mono.just(List.of(error));
    }
}
