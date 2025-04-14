package mapper;

// với T là entity, D là DTO
public interface GenericMapper<T, D> {
    D toDTO(T entity);
    T toEntity(D dto);
}

