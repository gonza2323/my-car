import { useEffect } from "react"
import {
  useGetPaises,
  useGetProvincias,
  useGetDepartamentos,
  useGetLocalidades
} from "@/hooks"

export function useUbicaciones(formValues) {
  const { data: paisResponse, isLoading: paisLoading } = useGetPaises(
    { query: { size: 99999 } } // countries probably don’t need a parent filter
  );

  const { data: provinciaResponse, isLoading: provinciaLoading } = useGetProvincias(
    { query: { size: 99999, paisId: formValues.paisId } },
    { enabled: !!formValues.paisId } // only fetch if paisId is set
  );

  const { data: departamentoResponse, isLoading: departamentoLoading } = useGetDepartamentos(
    { query: { size: 99999, provinciaId: formValues.provinciaId } },
    { enabled: !!formValues.provinciaId }
  );

  const { data: localidadResponse, isLoading: localidadLoading } = useGetLocalidades(
    { query: { size: 99999, departamentoId: formValues.departamentoId } },
    { enabled: !!formValues.departamentoId }
  );

  return {
    pais: {
      options: paisResponse?.data ?? [],
      value: formValues.paisId,
      loading: paisLoading,
      disabled: false,
    },
    provincia: {
      options: provinciaResponse?.data ?? [],
      value: formValues.provinciaId,
      loading: provinciaLoading,
      disabled: !formValues.paisId,
    },
    departamento: {
      options: departamentoResponse?.data ?? [],
      value: formValues.departamentoId,
      loading: departamentoLoading,
      disabled: !formValues.provinciaId,
    },
    localidad: {
      options: localidadResponse?.data ?? [],
      value: formValues.localidadId,
      loading: localidadLoading,
      disabled: !formValues.departamentoId,
    },
  };
}

