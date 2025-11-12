import { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { app } from '@/config';
import { useAuth } from '@/hooks/use-auth';

interface AuthGuardProps {
  children: ReactNode;
}

export function AuthGuard({ children }: AuthGuardProps) {
  const { pathname } = useLocation();
  const { isLoading, isAuthenticated } = useAuth();

  if (isLoading) {
    // return <LoadingScreen />;
    return "Loading.."
  }

  if (!isAuthenticated) {
    return (
      <Navigate to={`/login?r=${pathname}`} replace />
    );
  }

  return children;
}
