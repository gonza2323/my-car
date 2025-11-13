import { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { app } from '@/config';
import { useAuth } from '@/hooks/use-auth';

export function CompleteProfileGuard({ children }) {
  const { pathname } = useLocation();
  const { isLoading, isAuthenticated, hasCompletedProfile } = useAuth();

  if (isLoading) {
    // return <LoadingScreen />;
    return "Loading.."
  }

  if (isAuthenticated && !hasCompletedProfile) {
    return (
      <Navigate to={`/complete-profile`} replace />
    );
  }

  return children;
}
