import React from 'react';
import { ApiErrorState } from '../../components/common/ApiErrorState/ApiErrorState';
export const ApiErrorPage: React.FC = () => <ApiErrorState fullPage onRetry={() => window.location.reload()} />;
