import { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { app } from '@/config';
import { useAuth } from '@/hooks/use-auth';
import { Loader } from '@mantine/core';

interface AuthGuardProps {
  children: ReactNode;
}

export function AuthGuard({ children }: AuthGuardProps) {
  const { pathname } = useLocation();
  const { isLoading, isAuthenticated } = useAuth();

  if (isLoading) {
    return <Loader />;
  }

  if (!isAuthenticated) {
    return (
      <Navigate to={`/?r=${pathname}`} replace />
    );
  }

  return children;
}
