import {FC} from 'react';
import {Button} from '../ui';
import {Plus} from 'lucide-react-native';

interface AddBlockButtonProps {
  onPress: () => void;
}

export const AddBlockButton: FC<AddBlockButtonProps> = ({onPress}) => {
  return (
    <Button
      onPress={onPress}
      sx={{
        borderRadius: 12,
        width: 50,
        height: 50,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}>
      <Plus size={30} />
    </Button>
  );
};
