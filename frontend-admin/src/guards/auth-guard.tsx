import { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { LoadingScreen } from '@/components/loading-screen';
import { app } from '@/config';
import { useAuth } from '@/hooks';
import { paths } from '@/routes';

interface AuthGuardProps {
  children: ReactNode;
}

export function AuthGuard({ children }: AuthGuardProps) {
  const { pathname } = useLocation();
  const { isLoading, isAuthenticated } = useAuth();

  if (isLoading) {
    return <LoadingScreen />;
  }

  if (!isAuthenticated) {
    return (
      <Navigate to={`${paths.auth.login}?${app.redirectQueryParamName}=${pathname}`} replace />
    );
  }

  return children;
}
