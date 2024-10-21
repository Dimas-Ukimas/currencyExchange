package util.converter;

public interface Converter<E, ReqDto, RespDto> {

    E convertToEntity(ReqDto dto);

    RespDto convertToDto(E entity);

}
